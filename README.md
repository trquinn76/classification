# Security Classifications

This project is intended to contain data models suitable for representing the Classification schema
of selected countries. The set of Five Eyes countries was the initial target. However, as I have been
unable to find sufficient public documentation on the Canadian Classification system, this project is
currently limited to Classification Schemes for:
- Australia
- New Zealand
- United Kingdom
- United States of America

All models are based on publicly available documentation. Classified information will not be used to
define the data models found in these projects. The public documentation in use will be copied into
the sub project folder for each Classification Scheme.

## Purpose

These models exists to deal with the issue that data stored in computer systems frequently needs to maintain the
classification of the data. This is typically done in ad hoc ways, so that every data structure and library stores it's
classification data in different formats. Which then need to be mapped from one format to another, whenever the
different data sets are merged. Also each ad hoc format tends to support only a limited part of classification marking
standard - leading to issues with converting between formats, and with the periodic need to enhance an existing
format with additional fields.

In these projects the model is intended to be comprehensive, and suitable for use as Data Transfer Objects. The models
are based on `record`'s, with `unmodifiable` collections, to ensure immutability. This also makes these models safe
for use in threaded environments.

## Australian Protective Markers

[README.md](./classification-aus/README.md).

> #### Maven Coordinates:
> 
>     <dependency>
>         <groupId>io.github.trquinn76</groupId>
>         <artifactId>classification-aus</artifactId>
>         <version>1.0.4</version>
>     </dependency>

## New Zealand Protective Markers

[README.md](./classification-nzl/README.md).

> #### Maven Coordinates:
> 
>     <dependency>
>         <groupId>io.github.trquinn76</groupId>
>         <artifactId>classification-nzl</artifactId>
>         <version>1.0.1</version>
>     </dependency>

## UK Security Classification Markers

[README.md](./classification-uk/README.md).

> #### Maven Coordinates:
> 
>     <dependency>
>         <groupId>io.github.trquinn76</groupId>
>         <artifactId>classification-uk</artifactId>
>         <version>1.0.0</version>
>     </dependency>

## USA Security Classification Markers

[README.md](./classification-usa/README.md).

> #### Maven Coordinates:
> 
>     <dependency>
>         <groupId>io.github.trquinn76</groupId>
>         <artifactId>classification-usa</artifactId>
>         <version>1.0.0</version>
>     </dependency>
