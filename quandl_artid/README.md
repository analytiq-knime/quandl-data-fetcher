KNIME node skeleton with sample code as described [here](https://tech.knime.org/developer-guide).

[![Build Status](https://travis-ci.org/analytiq/https://github.com/analytiq-knime/quandl-data-fetcher.git.svg?branch=master)](https://travis-ci.org/analytiq/https://github.com/analytiq-knime/quandl-data-fetcher.git)
[![SonarCloud Gate](https://sonarcloud.io/api/badges/gate?key=quandl_grpid:quandl_artid)](https://sonarcloud.io/dashboard?id=quandl_grpid:quandl_artid)
[![SonarCloud Coverage](https://sonarcloud.io/api/badges/measure?key=quandl_grpid:quandl_artid&metric=coverage)](https://sonarcloud.io/component_measures/domain/Coverage?id=quandl_grpid:quandl_artid)

This project uses [Eclipse Tycho](https://www.eclipse.org/tycho/) to perform build steps.

# Installation

Requirements:

* KNIME, https://www.knime.org, version 3.3 or higher

Steps to get the source KNIME node inside KNIME:

1. Goto Help > Install new software ... menu
2. Press add button
3. Fill text fields with url of update site which contains this node.
4. Select --all sites-- in `work with` pulldown
5. Select the node
6. Install software
7. Restart KNIME

# Usage

1. Create a new KNIME workflow.
2. Find node in Node navigator panel.
3. Drag node to workflow canvas.

# Build

To build the node extension and verify the tests run with the following command:
```
mvn verify
```

Make sure all code is commited as the snapshot version is determined by git commit timestamp.

An Eclipse update site will be made in `p2/target/repository` directory.
The update site can be used to perform a local installation.

## Continuous Integration

Configuration files to run Continuous Integration builds on Linux (Travis-CI), OS X (Travis-CI) and Windows (AppVeyor) are present.

See `./.travis.yml` file how to trigger a Travis-CI build for every push or pull request.
Also see `./.travis.yml` file how to perform a [SonarCloud](https://sonarcloud.io/) analysis and code coverage.

See `./appveyor.yml` file how to run on https://www.appveyor.com .

# Development

Steps to get development environment setup:

1. Download KNIME SDK from https://www.knime.org/downloads/overview
2. Install/extract KNIME SDK
3. Start KNIME SDK
4. Install m2e (Maven integration for Eclipse) + Test workflows in JUnit

    1. Goto Window -> Preferences -> Install/Update -> Available Software Sites
    2. Make sure the following Software Sites (or a version of them) are present otherwise add them:

      * http://update.knime.org/analytics-platform/3.3
      * http://download.eclipse.org/eclipse/updates/4.6
      * http://download.eclipse.org/releases/neon
      * https://3d-e-chem.github.io/updates (only required for 3D-e-Chem node)

    3. Goto Help -> Check for updates
    4. Install any updates found & restart
    5. Goto Help > Install new software ...
    6. Select --All Available sites-- in work with pulldown
    7. Wait for list to be filled, `Pending...` should disappear
    8. Select the following items:

      * m2e - Maven integration for Eclipse (includes Incubating components)
      * Test KNIME workflows from a Junit test
      * Splash & node category for 3D-e-Chem KNIME nodes (only required for 3D-e-Chem node)

    9. Install software & restart

5. Import this repo as an Existing Maven project

During import the Tycho Eclipse providers must be installed, Eclipse will ask for your permission.

## Tests

Tests for the node are in `tests/src` directory.
Tests can be executed with `mvn verify`, they will be run in a separate KNIME environment.
Test results will be written to `test/target/surefire-reports` directory.
Code coverage reports (html+xml) can be found in the `tests/target/jacoco/report/` directory.

The tests can be run against a different KNIME version using `mvn verify -Dknime.version=3.5` where `3.5` is the major.minor version of KNIME.

### Unit tests

Unit tests written in Junit4 format can be put in `tests/src/java`.

### Workflow tests

See https://github.com/3D-e-Chem/knime-testflow#3-add-test-workflow

## Speed up builds

Running mvn commands can take a long time as Tycho fetches indices of all p2 update sites.
This can be skipped by running maven offline using `mvn -o`.

# New release

1. Update versions in pom files with `mvn org.eclipse.tycho:tycho-versions-plugin:set-version -DnewVersion=<version>-SNAPSHOT` command.
2. Create package with `mvn package`, will create update site in `p2/target/repository`
3. Run tests with `mvn verify`
4. Optionally, test node by installing it in KNIME from a local update site
5. Append new release to an update site
  1. Make clone of an update site repo
  2. Append release to the update site with `mvn install -Dtarget.update.site=<path to update site>`
6. Commit and push changes in this repo and update site repo.
7. Create a Github release
8. Update Zenodo entry
  1. Correct authors
  2. Correct license
9. Make nodes available to 3D-e-Chem KNIME feature by following steps at https://github.com/3D-e-Chem/knime-node-collection#new-release
