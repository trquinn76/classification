# USA Classification Marking Model

This project contains a data model for USA Classification Markings, as defined in the
[DoD Information Security Program: Marking of Information](./MarkingOfInformation/README.md).

This model is derived solely from publicly available documents. The set of documents referenced when designing this
model may be found in [Documents](./documents/README.md). These documents are:
- [DoD Information Security Program: Marking of Information](./documents/MarkingOfInformation/README.md)
- [Intelligence Community Markings System Register and Manual](./documents/ICMarkingsSystem/README.md)

> #### Maven Coordinates:
> 
>     <dependency>
>         <groupId>io.github.trquinn76</groupId>
>         <artifactId>classification-usa</artifactId>
>         <version>1.0.0</version>
>     </dependency>

### Purpose

This model exists to deal with the issue that data stored in computer systems frequently needs to maintain the
classification of the data. This is typically done in ad hoc ways, so that every data structure and library stores it's
classification data in different formats. Which then need to be mapped from one format to another, whenever the
different data sets are merged. Also each ad hoc format tends to support only a limited part of classification marking
standard - leading to issues with converting between formats, and with the periodic need to enhance an existing
format with additional fields.

In this project the model is intended to be comprehensive, and suitable for use as Data Transfer Objects. The models
are based on `record`'s, with `unmodifiable` collections, to ensure immutability. This also makes these models safe
for use in threaded environments.

## The Two purposes of the `ClassificationMarkerBuilder`

### Creating new ClassificationMarker's

To create new `ClassificationMarker`'s, first create an instance of `ClassificationMarkerBuilder`. Populate the fields
of the builder, and then use the `build()` function to create a new `ClassificationMarker`. Use of the builder ensures
that the resulting `ClassificationMarker` is valid.

#### Sub Builders

The `ClassificationMarkerBuilder` contains a number of sub builders to help manage the large number of options and
variables used to build a `ClassificationMarker`. These are:
* `modifier` - `ClassificationModifierBuilder`
* `sci` - `SCIBuilder`
* `sap` - `SAPBuilder`
* `aea` - `AtomicEnergyActInformationBuilder`
* `fgi` - `FGIBuilder`
* `disseminations` - `DisseminationsBuidler`
* `otherDisseminations` - `OtherDisseminationsBuilder`

Use these sub builders to populate values in the respective parts of the `ClassificationMarker`.

### Handling Invalid States (particularly in UI's)

In the course of working with Classified data, it is not unusual to need to hold representations of 
`ClassificationMarker`'s in invalid, intermediate states, while the User edits them via the UI. One of the
`ClassificationMarkerBuilder`'s purposes is to be able to hold `ClassificationMarker` information in an invalid state, while a
User is actively editing it via a UI.

## ClassificationMarker structure

The `ClassificationMarker` `record` consists of the following fields:

### Classification

The `Classification` for this `ClassificationMarker`. The list of Classifications is derived from the
[DoD Information Security Program: Marking of Information](./documents/MarkingOfInformation/README.md) 
**Enclosure 4 Section 4.a.2 page 67**, with **Enclosure 4 Section 3.a page 66** clearly defining the exclusively USA
classifications.

The `Classification` is required.

The Classifications are:
- UNCLASSIFIED
- RESTRICTED (only valid for use when the Marker has been modified to represent an international classification)
- CONFIDENTIAL
- SECRET
- TOP SECRET

The `Classification` record is required to be built with a String which is the `name()` of one of the entries in the
currently configured Classification enumeration.

##### Default Classifications

By default the library is configured to use a set of `DevelopmentClassification`'s. These are deliberately distinct
from the real set of `Classification`'s (`SecurityClassification`). Configuring `ProductionMode` to be true will cause the
library to start using the real set of `Classification`'s.

`DevelopmentClassification`'s exist to allow development and testing of Classification related logic and data storage
in less secure environments than the final target production environment.

### Classification Modifiers

#### Joint

Represents data which is jointly controlled by the USA and at least one additional foreign country. 
* Does NOT allow the use of the RESTRICTED classification, as this is not valid in the context of the USA.
* Does NOT allow the use of the NOFORN dissemination mark.
* Requires the list of JOINT countries to include the USA.
* Releasable To lists do not have to exactly match the JOINT list of countries. This library is not currently enforcing
any rules about this.

#### NATO (including COSMIC)

Indicates data which is protected in the NATO security system.
* Does allow the use of the RESTRICTED classification.
* Does NOT allow the use of the NOFORN dissemination mark.
* COSMIC is used for TOP SECRET instead of NATO.
* A NATO special mark may be included. Published values for these marks are ATOMAL and BOHEMIA. The special mark is an
arbitrary string, and can support any value.

#### Foreign

Indicates data/documents which are entirely of foreign origin. Should show the foreign Classification mark.
* Does allow the use of the RESTRICTED classification.
* Does NOT allow the use of the NOFORN dissemination.

Consider using the Additional Markings list for those foreign markings which do not conform to USA markings.

### Sensitive Compartmented Information

From [DoD Information Security Program: Marking of Information](./documents/MarkingOfInformation/README.md):
> SCI is classified national intelligence information concerning, or derived from, intelligence sources, methods or analytical processes that require handling within formal access control systems established by the Director of National Intelligence (DNI).

In this library SCI is modeled with a `Control System - Compartments - Sub Compartments` structure.

Quoting from [DoD Information Security Program: Marking of Information](./documents/MarkingOfInformation/README.md):
> SCI, regardless of classification level, must be processed only on an information system accredited for SCI processing (e.g., JWICS) and may not be processed, transferred to, or stored on SIPRNET, even if the information’s classification is at the SECRET level (e.g., SECRET//SI), as SIPRNET is not accredited for SCI.

To assist with handling this, as with Classification, when the library is NOT in production mode, any SCI Control
System name is given a Prefix, which is used to indicate that it is not a `Real` SCI Control System, but rather a
development value which is standing in for real values. This allows development of code which handles and manipulates
SCI in networks which are not otherwise accredited for SCI.

However, this will not prevent a data spill if unpublished SCI values are used in these development networks. Even with
a development Prefix, putting an unpublished SCI on a network not accredited for it will cause Security Officers to
come and talk at you about it, and create more paper work for lots of people. So don't do that.

Published SCI Control Systems, and their published Compartments, which have some degree of support in this library are:
* HCS (HUMINT Control System)
    * OPERATIONS
    * PRODUCT
* Special Intelligence (SI)
    * ECRU
    * GAMMA
    * NONBOOK
* TALENT KEYHOLE (TK)
    * GEOCAP
    * BLUEFISH
    * IDITAROD
    * KANDIK

The support for these Control Systems, includes named functions in the SCI Builder. The support for the Components
is String constants in the `Utils` class.

#### NOFORN Requirements

The NOFORN dissemination MUST be used with `HCS` or `TK-GEOCAP`, and the `ClassificationMarkerBuilder` enforces this.

### Special Access Program

From [DoD Information Security Program: Marking of Information](./documents/MarkingOfInformation/README.md):
> SAP control markings used in the banner line and at portions denote classified information that requires extraordinary protection

In this library SAP is modeled with a `Special Access Program - Compartments - Sub Compartments` structure. It is not
clear from [DoD Information Security Program: Marking of Information](./documents/MarkingOfInformation/README.md) that this
structure is necessary, but it is found in 
[Intelligence Community Markings System Register and Manual](./documents/ICMarkingsSystem/README.md). If the Compartments and
Sub Compartments are unnecessary, they may be omitted.

Quoting [DoD Information Security Program: Marking of Information](./documents/MarkingOfInformation/README.md):
> SAP information, regardless of classification, shall be processed only on an information system accredited for SAP processing, and operating at a classification level that meets or exceeds the classification level of the SAP data.

To assist with handling this, as with Classification, when the library is NOT in production mode, any Special Access
Program name is given a Prefix, which is used to indicate that it is not a `Real` Special Access Program, but rather a
development value which is standing in for real values. This allows development of code which handles and manipulates
SAP in networks which are not otherwise accredited for SAP.

When the `Handle via Special Access Channels Only (HVSACO)` mark is used, it is placed in the Additional Markings
section of the `ClassificationMarker`.

### Atomic Energy Agency Information

Indicates data which is related to nuclear materials. The library supports:
* Restricted Data
* Restricted Data CNWDI
* Restricted Data Sigma [sigma numbers]
* Formally Restricted Data
* Formally Restricted Data Sigma [sigma numbers]
* DOD UCNI (may no longer be in use)
* DOE UCNI (may no longer be in use)
* Transclassified Foreign Nuclear Information

### Foreign Government Information

From [DoD Information Security Program: Marking of Information](./documents/MarkingOfInformation/README.md):
> FGI markings are used in U.S. products to denote the presence of foreign-controlled information.

If the FGI mark has been explicitly set as concealed, then the country list must be empty.

### Disseminations

The list of Dissemination marks supported by this library are:
* WAIVED - specifically the Special Access Program Waived mark, which requires the presence of SAP values to be valid
* FOUO
* CONTROLLED_UNCLASSIFIED_INFORMATION (CUI)
* ORIGINATOR_CONTROLLED (ORCON)
* RELEASE_TO (REL TO)
* DISPLAY_ONLY
* CONTROLLED_IMAGERY (IMCON)
* NOFORN - Not Releasable to Foreign Nationals
* PROPRIETARY_INFORMATION (PROPIN)
* RELIDO - Releasable by Information Disclosure Official
* FISA - Foreign Intelligence Surveillance Act

### Other Disseminations

The list of Other Dissemination marks supported by this library are:
* ACCM - Alternative Compensatory Control Measures
* EXCLUSIVE_DISTRIBUTION (EXDIS) - mutually exclusive with NODIS
* NO_DISTRIBUTION (NODIS) = mutually exclusive with EXDIS
* SENSITIVE_BUT_UNCLASSIFIED (SBU) - mutually exclusive with SBU-NF
* SENSITIVE_BUT_UNCLASSIFIED_NOFORN (SBU-NF) mutually exclusive with SBU

### Additional Markings

Additional Markings is not part of the official Classification scheme. It exists to allow the addition of arbitrary
strings to the Classification Marker. For example SAP's `HVSACO` mark is placed here if it is set. The is also
be an appropriate place to add distribution instructions for the `NODIS` Other Dissemination mark.

In particular when the FOREIGN modifier is in use, Additional Markings may be used for any foreign markings which
do not map to an American marking.

## Configuration

##### Production Mode

Determines if the library is operating in Production Mode. When true causes the library to use `SecurityClassification`'s,
otherwise `DevelopmentClassification`'s are used. Defaults to `false`. This configuration value is shared with other
Classification libraries.
- Cmd Line Property: `classificationProductionMode`
- Environment Variable: `CLASSIFICATION_PRODUCTION_MODE`
- Config File Property: `io.github.trquinn76.classification.production.mode`

##### Config File Location

Specifies a User defined Config File.
- Cmd Line Property: `classificationConfigFile`
- Environment Variable: `CLASSIFICATION_CONFIG_FILE`

##### Development UNCLASSIFIED String

Determines the String used when displaying the `DEVELOPMENT_UNCLASSIFIED` Classification.
- Cmd Line Property: `classificationUsaDevelUnclassified`
- Environment Variable: `CLASSIFICATION_USA_DEVEL_UNCLASSIFIED`
- Config File Property: `io.github.trquinn76.classification.usa.development.unclassified.name`

##### Development RESTRICTED String

Determines the String used when displaying the `DEVELOPMENT_RESTRICTED` Classification.
- Cmd Line Property: `classificationUsaDevelRestricted`
- Environment Variable: `CLASSIFICATION_USA_DEVEL_RESTRICTED`
- Config File Property: `io.github.trquinn76.classification.usa.development.restricted.name`

##### Development CONFIDENTAL String

Determines the String used when displaying the `DEVELOPMENT_CONFIDENTIAL` Classification.
- Cmd Line Property: `classificationUsaDevelConfidential`
- Environment Variable: `CLASSIFICATION_USA_DEVEL_CONFIDENTIAL`
- Config File Property: `io.github.trquinn76.classification.usa.development.confidential.name`

##### Development SECRET String

Determines the String used when displaying the `DEVELOPMENT_SECRET` Classification.
- Cmd Line Property: `classificationUsaDevelSecret`
- Environment Variable: `CLASSIFICATION_USA_DEVEL_SECRET`
- Config File Property: `io.github.trquinn76.classification.usa.development.secret.name`

##### Development TOP SECRET String

Determines the String used when displaying the `DEVELOPMENT_TOP_SECRET` Classification.
- Cmd Line Property: `classificationUsaDevelTopSecret`
- Environment Variable: `CLASSIFICATION_USA_DEVEL_TOP_SECRET`
- Config File Property: `io.github.trquinn76.classification.usa.development.top.secret.name`

##### Development SCI Prefix

Determines the String prefixed onto SCI Control Systems when operating in development mode.
- Cmd Line Property: `classificationUsaDevelSciPrefix`
- Environment Variable: `CLASSIFICATION_USA_DEVEL_SCI_PREFIX`
- Config File Property: `io.github.trquinn76.classification.usa.development.sci.prefix`

##### Development SAP Prefix

Determines the String prefixed onto Special Access Programs when operating in development mode.
- Cmd Line Property: `classificationUsaDevelSapPrefix`
- Environment Variable: `CLASSIFICATION_USA_DEVEL_SAP_PREFIX`
- Config File Property: `io.github.trquinn76.classification.usa.development.sap.prefix`

#### Config precedence

The order of precedence for configuration values are:
- Cmd Line Property
- Environment Variable
- Configuration File value
- Default value

When searching configuration files, the library will search for files in the following order:
- file defined via the `classificationConfigFile` command line property. eg:
`java -DclassificationConfigFile=myconfig.properties MyApp`. Or the `CLASSIFICATION_CONFIG_FILE` environment variable.
- `application.properties`: this config file name is used by both **SpringBoot** and **Quarkus**.
- `classification-config.properties`: the developer can create this file anywhere on the classpath and it should be
successfully read in.
- `usa-default-classification-config.properties`: which already exists in the library `JAR` file, and holds the default
values used in this library.

#### Configuring to use real/Security Classifications

By default the library will use the `DevelopmentClassification`'s, rather than the real Classifications defined in
`SecurityClassification`. In order to use the `SecurityClassification`'s, it is necessary to set the Production Mode config
value. This can be done via runtime command line property, environment variable or configuration.

eg:
- `java -DclassificationProductionMode=true MyApp`
- `CLASSIFICATION_PRODUCTION_MODE=true`
- in config file `application.properties` set: `io.github.trquinn76.classification.production.mode = true`

## Implementation considerations

### Sets for Lists

There are a number of `List`'s in the `ClassificationMarkerBuilder` which are implemented as `TreeSet`'s. These `Set`'s are
converted to true `List`'s when the `ClassificationMarker` is built. Their use ensures that duplicate values are handled
and that the generated `List`'s are in alphabetical order.

### ClassificationMarker's `toString()` function

The `toString()` function in `ClassificationMarker` is intended to produce a String which would be suitable for
use as a document banner. However, this function deviates from defined format for this String in a couple of ways.

1. In a document banner, if there are 3 or more SAP's, then the banner should show `SAR-MULTIPLE PROGRAMS`, with the
list of Programs shown elsewhere. However, because this library is unable to display a list of Programs elsewhere, it
will show the full set in the String, no matter how many there are.
2. Additional Markings is not a part of the Classification markings scheme, so there is no place for it in the defined
format. This library will append the Strings in Additional Markings in alphabetical order, after all other sections
have been added.

### Country Lists

In this library, a country list is expected to be a list of 3 letter country codes, and 4 letter organisation codes.
For example: USA, CAN, GBR, NATO. In most such lists, the USA country code will be displayed first, with the rest
appearing in the order: shorter strings before longer strings, and then alphabetical order.

### Atomic Energy Agency Information requiring NOFORN

According to [Intelligence Community Markings System Register and Manual](./documents/ICMarkingsSystem/README.md) all
Restricted Data (including CNWDI and SIGMA) and Formally Restricted Data (including SIGMA) require the
NOFORN Dissemination mark, unless there is a sharing agreement. Meanwhile the
[DoD Information Security Program: Marking of Information](./documents/MarkingOfInformation/README.md) document does not
mention this requirement.

In this library, the lack of NOFORN will be detected for these AEA Markings, and a WARNING will be logged rather
than an error raised.
