package edu.iu.psgd;

import com.google.common.base.Stopwatch;
import edu.iu.psgd.api.data.DataSet;
import edu.iu.psgd.constant.SVMType;
import edu.iu.psgd.exceptions.MatrixMultiplicationException;
import edu.iu.psgd.exceptions.NullDataSetException;
import edu.iu.psgd.math.Matrix;
import edu.iu.psgd.parallel.svm.adam.AdamPSGD;
import edu.iu.psgd.parallel.svm.adam.AdamSGD;
import edu.iu.psgd.parallel.svm.pegasos.PegasosSGD;
import edu.iu.psgd.predict.Predict;
import edu.iu.psgd.resource.ResourceManager;
import edu.iu.psgd.util.BlassUtils;
import edu.iu.psgd.util.OptArgs;
import edu.iu.psgd.util.Params;
import edu.iu.psgd.util.Utils;
import mpi.MPI;
import mpi.MPIException;
import org.apache.commons.cli.ParseException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Program {

    private static final Logger LOG = Logger.getLogger(Program.class.getName());

    private static final double B2MB = 1024.0 * 1024.0;

    private static final double N2S = 1000000000;

    private static OptArgs optArgs;

    private static Params params;

    public static void main(String[] args) throws NullDataSetException, ParseException, MatrixMultiplicationException, MPIException, IOException {

        //parallelAdam(args);
        //sequentialAdam(args);
        //parallelAdamDistributedLoad(args);
        optArgs = new OptArgs(args);
        optArgs.getArgs();
        params = optArgs.getParams();

        LOG.info("SVM_TYPE : " + params.getSvmType());

        if (params.getSvmType() == SVMType.DEFAULT) {
            distributedSVM(args);
        }

        if (params.getSvmType() == SVMType.ENSEMBLE) {
            distributedEnsembleSVM(args);
        }

        if (params.getSvmType() == SVMType.OTHER) {
            openBlasBenchmark();
        }

        if(params.getSvmType() == SVMType.BLAS) {
            blasDistributedSVM(args);
        }


    }

    public static void parallelAdamDistributedLoad(String[] args) throws MPIException, ParseException, NullDataSetException, MatrixMultiplicationException {
        MPI.Init(args);

        int world_rank = MPI.COMM_WORLD.getRank();
        int world_size = MPI.COMM_WORLD.getSize();
        params = optArgs.getParams();
        ResourceManager resourceManager = new ResourceManager(params, world_rank, world_size);
        DataSet dataSet = resourceManager.distributedLoad();
        double[][] X = dataSet.getXtrain();
        //Matrix.printMatrix(X);
        double[] y = dataSet.getYtrain();
        double startTime = MPI.wtime();
        AdamPSGD adamPSGD = new AdamPSGD(X, y, 0.01, params.getIterations(), 0.5, 0.5, world_size, world_rank);
        adamPSGD.sgd();
        double endTime = MPI.wtime();
        if (world_rank == 0) {
            System.out.println("Time Taken : " + (endTime - startTime));
        }
        MPI.COMM_WORLD.barrier();
        MPI.Finalize();
    }

    public static void distributedEnsembleSVM(String[] args) throws MPIException, ParseException, NullDataSetException, MatrixMultiplicationException, IOException {
        MPI.Init(args);
        double t1 = 0;
        int world_rank = MPI.COMM_WORLD.getRank();
        int world_size = MPI.COMM_WORLD.getSize();
//        LOG.info(String.format("Rank[%d] Total Memory %f MB, Max Memory %f MB", world_rank,
//                ((double)Runtime.getRuntime().totalMemory())/B2MB, ((double)Runtime.getRuntime().maxMemory())/B2MB));
        params = optArgs.getParams();
        ResourceManager resourceManager = new ResourceManager(params, world_rank, world_size);
        double dataLoadingTime = 0.0;
        t1 = System.currentTimeMillis();
        DataSet dataSet = resourceManager.distributedLoad();
        double[][] X = dataSet.getXtrain();
        //Matrix.printMatrix(X);
        double[] y = dataSet.getYtrain();
        /*if (world_rank == 0) {
            LOG.info(String.format("Data Loading Completed, X[%d,%d], y[%d]", X.length, X[0].length, y.length));
        }*/
        dataLoadingTime = System.currentTimeMillis() - t1;
        double trainingTime = 0.0;
       /* if (world_rank == 0) {
            LOG.info(String.format("Training Started"));
        }*/
        t1 = System.currentTimeMillis();
        double t2 = MPI.wtime();
        PegasosSGD pegasosSGD = new PegasosSGD(X, y, params.getAlpha(), params.getIterations());
        pegasosSGD.setWorldRank(world_rank);
        pegasosSGD.setWorld_size(world_size);
        pegasosSGD.setDoLog(false);
        pegasosSGD.sgdEnsemble();
        double[] w = pegasosSGD.getW();
        double[] globalW = new double[w.length];
        try {
            MPI.COMM_WORLD.allReduce(w, globalW, 1, MPI.DOUBLE, MPI.SUM);
            //MPI.COMM_WORLD.reduce(w, globalW, 1, MPI.DOUBLE, MPI.SUM, 0);
        } catch (MPIException e) {
            System.out.println("Exception : " + e.getMessage());
        }
        double t3 = MPI.wtime();
        trainingTime = System.currentTimeMillis() - t1;
        trainingTime /= 1000.0;
        dataLoadingTime /= 1000.0;
        double trainingTimeWT = t3 - t2;
        LOG.info(String.format("Rank[%d][%d] Total Memory %f MB, Max Memory %f MB, Training Completed! => Data Loading Time %f , Training Time : %f ", world_rank, y.length, ((double) Runtime.getRuntime().totalMemory()) / B2MB, ((double) Runtime.getRuntime().maxMemory()) / B2MB,
                dataLoadingTime, trainingTime));
        System.out.println(String.format("Sys Time : %f, WTime : %f", trainingTime, trainingTimeWT));
        if (world_rank == 0) {
            double[] wFinal = Matrix.scalarDivide(globalW, world_size);
            Utils.logSave(params, trainingTime, dataLoadingTime);
        }
        //MPI.COMM_WORLD.barrier();
        System.gc();
        MPI.Finalize();

    }

    public static void distributedSVM(String[] args) throws MPIException, ParseException, NullDataSetException, MatrixMultiplicationException, IOException {
        MPI.Init(args);


        long t1 = 0;
        int world_rank = MPI.COMM_WORLD.getRank();
        int world_size = MPI.COMM_WORLD.getSize();
        if(world_rank == 0) {
            LOG.info("Distributed SVM DEFAULT");
        }

//        LOG.info(String.format("Rank[%d] Total Memory %f MB, Max Memory %f MB", world_rank,
//                ((double)Runtime.getRuntime().totalMemory())/B2MB, ((double)Runtime.getRuntime().maxMemory())/B2MB));
        params = optArgs.getParams();
        ResourceManager resourceManager = new ResourceManager(params, world_rank, world_size);
        long dataLoadingTime = (long) 0.0;
        t1 = System.nanoTime();
        DataSet dataSet = resourceManager.distributedLoad();
        MPI.COMM_WORLD.barrier();
        //TODO :: off heap array definition
        double[][] X = dataSet.getXtrain();
        //Matrix.printMatrix(X);
        double[] y = dataSet.getYtrain();
        if (world_rank == 0) {
            LOG.info(String.format("Data Loading Completed, X[%d,%d], y[%d]", X.length, X[0].length, y.length));
        }
        dataLoadingTime = System.nanoTime() - t1;
        long trainingTime = (long) 0.0;
       /* if (world_rank == 0) {
            LOG.info(String.format("Training Started"));
        }*/
        double t2 = MPI.wtime();
        t1 = System.nanoTime();
        PegasosSGD pegasosSGD = new PegasosSGD(X, y, params.getAlpha(), params.getIterations());
        pegasosSGD.setWorldRank(world_rank);
        pegasosSGD.setWorld_size(world_size);
        pegasosSGD.setDoLog(false);
        pegasosSGD.sgd();
        double[] w = pegasosSGD.getW();
        MPI.COMM_WORLD.barrier();
        double t3 = MPI.wtime();
        double trainingTimeWT = t3 - t2;
        double commTime = pegasosSGD.communicationTime;
        double compTime = trainingTimeWT - commTime;
        trainingTime = System.nanoTime() - t1;
        trainingTime /= N2S;
        dataLoadingTime /= N2S;
        double trainingTimeD = (double) trainingTime;
        double dataLoadingTimeD = (double) dataLoadingTime;
//        LOG.info(String.format("Rank[%d][%d] Total Memory %f MB, Max Memory %f MB, Training Completed! => " +
//                        "Data Loading Time %f , Training Time : %f ", world_rank, y.length,
//                ((double) Runtime.getRuntime().totalMemory()) / B2MB, ((double) Runtime.getRuntime().maxMemory()) / B2MB,
//                dataLoadingTimeD, trainingTimeD));
        System.out.println(String.format("Sys Time : %f, WTime : %f \n", trainingTimeD, trainingTimeWT));
        if (world_rank == 0) {
            LOG.info("Distributed SVM DEFAULT");
            String s = "";
            s += "Comm Time = " + commTime + "\n";
            s += "Comp Time = " + compTime + "\n";
            s += "Training Time = " + trainingTimeWT + "\n";
            System.out.println(s);
            Utils.logSave(params, trainingTimeWT, dataLoadingTime);
        }
        //System.gc();
        MPI.Finalize();

    }

    public static void blasDistributedSVM(String[] args) throws MPIException, ParseException, NullDataSetException, MatrixMultiplicationException, IOException {
        MPI.Init(args);


        long t1 = 0;
        int world_rank = MPI.COMM_WORLD.getRank();
        int world_size = MPI.COMM_WORLD.getSize();
        if(world_rank == 0) {
            LOG.info("Distributed SVM BLAS");
        }

//        LOG.info(String.format("Rank[%d] Total Memory %f MB, Max Memory %f MB", world_rank,
//                ((double)Runtime.getRuntime().totalMemory())/B2MB, ((double)Runtime.getRuntime().maxMemory())/B2MB));
        params = optArgs.getParams();
        ResourceManager resourceManager = new ResourceManager(params, world_rank, world_size);
        long dataLoadingTime = (long) 0.0;
        t1 = System.nanoTime();
        DataSet dataSet = resourceManager.distributedLoad();
        MPI.COMM_WORLD.barrier();
        //TODO :: off heap array definition
        double[][] X = dataSet.getXtrain();
        //Matrix.printMatrix(X);
        double[] y = dataSet.getYtrain();
        if (world_rank == 0) {
            LOG.info(String.format("Data Loading Completed, X[%d,%d], y[%d]", X.length, X[0].length, y.length));
        }
        dataLoadingTime = System.nanoTime() - t1;
        long trainingTime = (long) 0.0;
       /* if (world_rank == 0) {
            LOG.info(String.format("Training Started"));
        }*/
        double t2 = MPI.wtime();
        t1 = System.nanoTime();
        final Stopwatch stopwatch = Stopwatch.createStarted();
        PegasosSGD pegasosSGD = new PegasosSGD(X, y, params.getAlpha(), params.getIterations());
        pegasosSGD.setWorldRank(world_rank);
        pegasosSGD.setWorld_size(world_size);
        pegasosSGD.setDoLog(false);
        pegasosSGD.bSgd();
        long t =  stopwatch.elapsed(TimeUnit.MILLISECONDS);
        System.out.println("Time in Seconds : " + (double) t / 1000.0);
        double[] w = pegasosSGD.getW();
        MPI.COMM_WORLD.barrier();
        double t3 = MPI.wtime();
        double trainingTimeWT = t3 - t2;
        double commTime = pegasosSGD.communicationTime;
        double compTime = trainingTimeWT - commTime;
        trainingTime = System.nanoTime() - t1;
        trainingTime /= N2S;
        dataLoadingTime /= N2S;
        double trainingTimeD = (double) trainingTime;
        double dataLoadingTimeD = (double) dataLoadingTime;
//        LOG.info(String.format("Rank[%d][%d] Total Memory %f MB, Max Memory %f MB, Training Completed! => " +
//                        "Data Loading Time %f , Training Time : %f ", world_rank, y.length,
//                ((double) Runtime.getRuntime().totalMemory()) / B2MB, ((double) Runtime.getRuntime().maxMemory()) / B2MB,
//                dataLoadingTimeD, trainingTimeD));
        System.out.println(String.format("Sys Time : %f, WTime : %f \n", trainingTimeD, trainingTimeWT));
        if (world_rank == 0) {
            LOG.info("Distributed SVM BLAS");
            String s = "";
            s += "Comm Time = " + commTime + "\n";
            s += "Comp Time = " + compTime + "\n";
            s += "Training Time = " + trainingTimeWT + "\n";
            System.out.println(s);
            Utils.logSave(params, trainingTimeWT, dataLoadingTime);
        }
        //System.gc();
        MPI.Finalize();

    }


    public static void parallelAdam(String[] args) throws MPIException, ParseException, NullDataSetException, MatrixMultiplicationException {
        MPI.Init(args);
        int world_rank = MPI.COMM_WORLD.getRank();
        int world_size = MPI.COMM_WORLD.getSize();
        OptArgs optArgs = new OptArgs(args);
        optArgs.getArgs();
        Params params = optArgs.getParams();
        ResourceManager resourceManager = new ResourceManager(params);
        DataSet dataSet = resourceManager.load();
        double[][] X = dataSet.getXtrain();
        //Matrix.printMatrix(X);
        double[] y = dataSet.getYtrain();

        double[][] Xtest = dataSet.getXtest();
        //Matrix.printMatrix(X);
        double[] ytest = dataSet.getYtest();
        double startTime = MPI.wtime();
        AdamPSGD adamPSGD = new AdamPSGD(X, y, 0.01, params.getIterations(), 0.5, 0.5, world_size, world_rank);
        adamPSGD.sgd();
        double endTime = MPI.wtime();
        if (world_rank == 0) {
            System.out.println("Time Taken : " + (endTime - startTime));
        }
        MPI.COMM_WORLD.barrier();
        MPI.Finalize();
    }

    public static void sequentialAdam(String[] args) throws ParseException, NullDataSetException, MatrixMultiplicationException {
        OptArgs optArgs = new OptArgs(args);
        optArgs.getArgs();
        Params params = optArgs.getParams();
        ResourceManager resourceManager = new ResourceManager(params);
        DataSet dataSet = resourceManager.load();
        double[][] X = dataSet.getXtrain();
        //Matrix.printMatrix(X);
        double[] y = dataSet.getYtrain();

        double[][] Xtest = dataSet.getXtest();
        //Matrix.printMatrix(X);
        double[] ytest = dataSet.getYtest();

        double[][] yp = new double[1][params.getFeatures()];
        yp[0] = y;
        //Matrix.printMatrix(yp);
        //PegasosSGD pegasosSGD = new PegasosSGD(X, y, params.getAlpha(), params.getIterations());
        //pegasosSGD.sgd();
        double[] beta1 = {0.5, 0.55, 0.6, 0.65, 0.7, 0.75, 0.8, 0.85, 0.90, 0.93, 0.95, 0.99, 0.999};
        double[] beta2 = {0.5, 0.55, 0.6, 0.65, 0.7, 0.75, 0.8, 0.85, 0.90, 0.93, 0.95, 0.99, 0.999};
        double[] alphas = {0.1, 0.01, 0.001, 0.00001, 0.000001};

//        for (int j = 5; j < beta1.length; j++) {
//            for (int k = 5; k < beta2.length; k++) {
//                for (int i = 0; i < alphas.length; i++) {
//                    AdamSGD adamSGD = new AdamSGD(X, y, alphas[i], params.getIterations(), beta1[j], beta2[k]);
//                    adamSGD.sgd();
//                    double[] wFinal = adamSGD.getW();
//                    Predict predict = new Predict(Xtest, ytest, wFinal);
//                    double acc = predict.predict();
//                    System.out.println("Prediction Accuracy : " + acc + " %");
//                }
//            }
//        }
        long startTime = System.currentTimeMillis();
        AdamSGD adamSGD = new AdamSGD(X, y, params.getAlpha(), params.getIterations(), 0.55, 0.75);
        adamSGD.sgd();
        long endTime = System.currentTimeMillis();
        double[] wFinal1 = adamSGD.getW();
        wFinal1 = Matrix.scalarMultiply(wFinal1, 0.1);
        Predict predict = new Predict(Xtest, ytest, wFinal1);
        double acc = predict.predict();
        System.out.println("Prediction Accuracy : " + acc + " %");
        System.out.println("Time Taken : " + (endTime - startTime) / 1000.0);
//        double[] wFinal = new double [] {1.567828781359268525e-04,-4.274419901471123578e-02,4.455232382565429972e-02,8.774471690792579048e-02,6.598134455421535149e-02,-8.745785953508246335e-02,9.000572222464389011e-03,4.140805375996165633e-03,1.883514808781848546e-03,1.067579700561681333e-02,1.160744071187512667e-02,-6.196187374953336310e-05,-6.827671623315374034e-05,5.583381934070298647e-02,6.180625797707874937e-03,-1.681420030671109192e-02,2.047809785070596141e-05,4.150158030752101185e-02,1.102910402903546777e-01,-1.073004863409281369e-02,2.852503622469095739e-04,-4.325408015445462301e-02,-6.975951295628808960e-05,1.739770471662364622e-03,-7.972430705654779369e-04,-1.227965828124542337e-02,-5.936161197901248035e-03,-7.823250641148373017e-04,3.332330627431392533e-03,4.020898488354505154e-05,-3.647063474568994212e-04,-7.105211998448908352e-05,-6.609554417140921321e-04,-6.266694468811222218e-04,-1.834310947677091422e-02,-4.325408015445462301e-02,-1.073004863409281369e-02,1.859030663146079730e-03,8.575158533553952633e-01,8.897712831586230831e-01,1.227047671412596561e-02,1.963855314152083715e-03,5.036254932255585437e-04,6.557933658055648766e-04,2.239549174636132692e-02,-6.869259699879817684e-04,-1.094531944389987889e-02,1.414426722239095144e-02,1.411231043336898597e-02,-3.139264789283543328e-02,-1.117572351508232080e-02,3.571442280885732401e-02,3.110171079313491546e-04,-1.249545181723026900e-02,2.267884163761098998e-02,1.550406431405347584e-04,1.365466362541953565e-04,1.920729393194497503e-04,-1.892426913744943862e-03,-2.245329110332663480e-04,1.111108958522599721e-02,-6.108482092837883145e-04,1.549508889415067148e-01,2.895274821018782585e-02,-1.550213397805527389e-04,-3.680566093656968056e-04,8.252265185795418234e-02,4.457708926584806174e-04,-5.350165970442657265e-04,-5.971976297234984389e-04,8.019051424694124616e-04,1.120634574263439272e-02,1.397928068598940365e-01,-1.078068620789078391e+00,3.968847732743967238e-02,-7.745004430505127990e-01,6.613608979589089953e-04,-7.755967027759729741e-04,-5.789682059883883647e-04,-1.505085010655635679e-02,1.839919280587820963e-02,1.437232145303142805e-01,7.419643839606974989e-02,-3.477404971816550321e-04,2.279778098554454844e-04,-1.696975611427491951e-04,3.948975261660108912e-04,8.271857552127298389e-04,-3.903742321228109626e-04,-6.560298498200743678e-04,4.732565828560772285e-04,3.439749311048504853e-04,3.127348040215671162e-05,-3.995845514094162435e-05,6.545060539421799443e-04,2.253337407607765633e-04,-6.449733130970486871e-04,-5.793447351407426341e-04,-3.394846083601829264e-05,2.479593207545798136e-04,2.546821694724660614e-05,-1.127888109019075302e-04,-2.544077617533628674e-04,4.394000990379793160e-04,-8.361780915100071754e-04,-6.244488710095810790e-04,-4.828708337488828574e-05,-8.534797650336041529e-04,-5.247381118881030743e-05,-6.990712079459970125e-04,-3.669131408118395804e-04,-5.176573678597922904e-05,4.502725113114048250e-04,1.478983191665432777e-04,9.590110916954559805e-05,-2.430472052734976686e-04,8.220678134308116421e-04,-3.510731588245151762e-05,8.502203187527922288e-04,3.115502004985197613e-04,3.362520648347040036e-04,-4.360379410814937788e-05,6.677649854291501166e-04};
//        Predict predict2 = new Predict(Xtest, ytest, wFinal);
//        double acc2 = predict2.predict();
//        System.out.println("Prediction Accuracy : " + acc2 + " %");
    }

    public static void openBlasBenchmark() {
        //BlassUtils.run();
        BlassUtils.withBlas();
        try {
            BlassUtils.withoutBlas();
        } catch (MatrixMultiplicationException e) {
            e.printStackTrace();
        }
    }
}
