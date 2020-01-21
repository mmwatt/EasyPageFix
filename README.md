# EasyPageFix

The code from my dissertation project, **"Automated Bug-Fixing of Website Layouts"**

Original idea and supervision from [Phil McMinn](https://mcminn.io/) @ The University of Sheffield

## Abstract

"The world wide web is becoming an increasingly large part of people’s lives and they are accessing it in increasingly diverse ways; through mobile phones, tablets, laptops and desktop PCs with various different browsers. It is therefore difficult for developers to account for these differences when designing a website. This project aims to produce a system which allows them to write a specification for a website, which should be able to fix problems quickly and easily, using search-based techniques, enabling them to spend more time on the content of the website, instead of the minor details of layout bugs."

## General Info

EasyPageFix is written in Java, using the internal API from the [Galen Framework](http://galenframework.com/), which allows for the checking of webpages against an easily readable specification. EasyPageFix then uses its own internal representation of the webpage and stylesheet to trial different CSS changes, producing a 'patch' of the most successful ones (that fix errors detected by Galen).

Currently the project has very basic functionality for detecting and fixing errors with elements on the screen. Your elements will need to have the same names as their names in the Galen Specification. 

In future, it might be possible to re-architect the detection system so it can use deeper 'linking' to the specification, allowing for more accurate CSS patches to be generated.

**Don't expect this to fix your website**. It is a proof-of-concept project which has many limitations.

## Setup/Installation

### .jar file

1. Ensure your Java classpath is set up correctly with a Java 1.8 runtime. Instructions [here](https://docs.oracle.com/javase/tutorial/essential/environment/paths.html)

2. Download the tool here (TODO link)

3. Create a local copy of your webpage, with files for HTML, CSS and any others you might have. Make sure all relevant styles are in a single CSS file.

4. Create a Galen specification for your webpage. Instructions [here](http://galenframework.com/docs/reference-galen-spec-language-guide/)

5. Run the jar:

   ```
   java -jar EasyPageFix.jar (arguments)
   ```

   The order of the arguments:

    ```
    0: Path to HTML file
    1: Path to CSS file
    2: Path to misc. resources for the page (page's root folder, normally)
    3: Path to galen spec
    4: Output path of fixed page
    5: Path to a chrome driver executable
    6: Chrome Window Width
    7: Chrome Window Height
    ```

### From source (to use example run configurations)



1. Clone the repo from this page. General instructions for Git are available [here](https://help.github.com/en/github/creating-cloning-and-archiving-repositories/cloning-a-repository).

   ```
   git clone https://github.com/mmwatt/EasyPageFix.git
   ```

2. Download the version of [ChromeDriver](https://chromedriver.chromium.org/) that corresponds to your version of Chrome or Chromium.

3. Open the project with an IDE like IntelliJ. This is recommended for the dependencies to be imported correctly, and to have access to some example run configurations.

   If you are not using IntelliJ or Maven, then the project needs to have the following dependencies available:

   - [Galen Framework](https://github.com/galenframework/galen) 2.4.4+ (might work with versions 2+ but untested)
   - [Java String Similarity](https://github.com/tdebatty/java-string-similarity) 1.2.1+
   - [CSS Parser](http://cssparser.sourceforge.net/) 0.9.27+
   - [SAC (Simple API for CSS)](https://www.w3.org/Style/CSS/SAC/) 1.3+
   - [Apache Commons IO](https://commons.apache.org/proper/commons-io/) 2.4+
   - [jsoup HTML parser](https://jsoup.org/) 1.12.1+

4. You can use one of the built-in run configurations to 'fix' problems with the sample page and specification. Please note, **these may not work fully, and exist to illustrate the tool's limitations**.

   Ensure your ChromeDriver is in the following path:

   ```
   EasyPageFix/drivers/chromedriver.exe
   ```

   It can be located elsewhere if you are running the tool yourself.

5. For your own run configurations, please use the following order for the command-line arguments

   ```
   0: Path to HTML file
   1: Path to CSS file
   2: Path to misc. resources for the page (page's root folder, normally)
   3: Path to galen spec
   4: Output path of fixed page
   5: Path to a chrome driver executable
   6: Chrome Window Width
   7: Chrome Window Height
   ```

Feel free to [open an issue](https://github.com/mmwatt/EasyPageFix/issues/new) if you're experiencing any problems. Please note that this project is mostly inactive, but I'll do what I can to help!

