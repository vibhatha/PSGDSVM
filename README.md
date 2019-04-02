# PSGDSVM
PSGD SVM

# BUILD

```bash
mvn clean install
```

# RUN 
Make sure resource/datasouce.yaml has the base path for all your datasets
Each dataset will have it's own folder.

For example epsilon

And within each folder there will be training.csv file which is a dense matrix
The format of the file must be as follows. 

```csv
label, feature_1, feature_2, ...., feature_d
```
Refer to the bin folder scripts. 
