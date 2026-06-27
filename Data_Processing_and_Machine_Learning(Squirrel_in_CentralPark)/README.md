# Overview

This project investigates whether squirrel behavioural patterns can predict approach behaviour toward humans, using the 2018 Central Park Squirrel Census dataset. The analysis combines individual-level squirrel observations with hectare-level environmental data to explore how behaviour, location, and warning signals relate to human-wildlife interaction.
Research Question: Can squirrel behaviour patterns predict whether a squirrel will approach humans in Central Park?

# Dataset

Source: 2018 Central Park Squirrel Census — NYC Open Data
Files used: squirrel.csv (individual behavioural observations) + hectare.csv (environmental context)
Final dataset: 3,023 samples, 24 features, no missing values after cleaning
Target variable: Approaches — severely imbalanced (5.9% True)

# Requirements
```
pandas
numpy
scikit-learn
matplotlib
seaborn
```
# Notebook Sections

The notebook runs top to bottom in four stages:

1. Preprocessing Drops high-missing columns, imputes remaining gaps, merges squirrel.csv + hectare.csv, engineers Hectare_Squirrel_Count, splits 80/20 before oversampling
2. Correlation AnalysisComputes Mutual Information (all features) and Pearson correlation (boolean behavioural features) against the Approaches target
3. Supervised LearningTrains KNN (k=5) and Decision Tree (max_depth=4) on RandomOverSampler-balanced training data; evaluates with accuracy/precision/recall/F1
4. Clustering Three K-Means/Hierarchical analyses — behavioural (K=5), geospatial (K=2), warning-signal (Ward linkage, K=3)


| Model | Accuracy | Precision | Recall | F1 |
|---|---|---|---|---|
| Majority Baseline | 0.94 | — | 0.00 | 0.00 |
| KNN (k=5) | 0.94 | 0.31 | 0.13 | 0.18 |
| Decision Tree (d=4) | 0.68 | 0.12 | **0.77** | 0.20 |

Full methodology and discussion in [`Can_squirrel_behaviour_patterns_predict_Report.pdf`](Data_Processing_and_Machine_Learning(Squirrel_in_CentralPark)/Can_squirrel_behaviour_patterns_predict_Report.pdf).
