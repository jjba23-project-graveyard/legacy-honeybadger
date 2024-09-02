
# Table of Contents

1.  [Honeybadger](#orgdd8181c)
    1.  [Examples](#org577f0ed)
    2.  [License](#org3bd570c)
    3.  [Disclaimer](#orgab264f0)
    4.  [Usage](#org510ce6b)
    5.  [Colors](#org8dcd572)
    6.  [Using with config files](#org4bbb5c4)
    7.  [Using Programatically](#org0339297)
    8.  [Supported formats](#org0e23a49)



<a id="orgdd8181c"></a>

# Honeybadger

![img](./dist/scala.png)

Scala CLI utility that aims to generate beautiful images as badges to embed in your favourite places.

This project mostly aims to be used to add beautiful badges to your org or markdown files, or websites.

Lots of options are possible, but sane defaults are provided. We support many popular technologies and brands, but everything is designed so that it is very easy to add your own.

Feel free to use the content in `dist` and `resources` at will.


<a id="org577f0ed"></a>

## Examples

If you would like to see some examples showing the badge generation possibilities, take a look at [the BADGES.md file](./resources/examples/BADGES.md).


<a id="org3bd570c"></a>

## License

This software is distributed under the GNU General Public License (GPL) v3.0 or later.

![img](./resources/branding/mellivora-capensis.jpg)


<a id="orgab264f0"></a>

## Disclaimer

The Honeybadger project and its authors and contributors do not own the logos or branding used to create the badges, we simply provide a convenient way to use them. The copyright of all imagery and related is property of the respective authors.


<a id="org510ce6b"></a>

## Usage

This project has been envisioned as a CLI-first utility. In the future this might grow in other directions, and we are open for it, provided there is a use-case. The codebase is quite adaptable and should be easy to modify.

As of now, this utility can accept input either programatically or via config files.


<a id="org8dcd572"></a>

## Colors

HoneyBadger includes several predefined colors, like Catppuccin, that can be accessed via slug.

See all the possibilities [here in the code](./src/main/scala/Palette.scala).
You can also specify custom colors, via hex code, e.g. #eeeeee or #fd39ab


<a id="org4bbb5c4"></a>

## Using with config files

TODO: Compile utility to native binary and bundle license + instructions

You can run this utility with SBT installed and you must specify one or more config files (JSON and/or YAML):

    # single file
    sbt "run ./resources/examples/tasks.yaml"
    # multiple files and formats
    sbt "run ./resources/examples/tasks.json ./resources/examples/tasks.yaml"

For the best way to understand the possibilities in the YAML / JSON file, please reference the source code where we defined the data models at [./src/main/scala/Entities.scala](./src/main/scala/Entities.scala) .

An example YAML file is shown below.
Alternatively see [the example file](./resources/examples/tasks.yaml) in the repo.

    
    leftRightBadgeTasks:
      - leftBadgeParameters:
          textContents: SCALA
          textColor: text-catppuccin-mocha
          backgroundColor: mantle-catppuccin-mocha
          width: 76
          height: 24
          fontSize: 12
          backgroundColorBrightnessModifier:
          customFontFilePath: "./resources/fonts/SofiaSans-Black.ttf"
          iconParameters:
            width: 16
            height: 16
            filePath: "./resources/icons/scala.svg"
        rightBadgeParameters:
          textContents: v2.13.8
          textColor: text-catppuccin-mocha
          backgroundColor: red-catppuccin-latte
          width: 50
          height: 24
          fontSize: 12
          backgroundColorBrightnessModifier:
          customFontFilePath: "./resources/fonts/SofiaSans-Black.ttf"
          iconParameters:
        destinationFilePath: "./dist/scala.png"
        destinationFileExtension: png
    singleBadgeTasks:
      - badgeParameters:
          textContents: HASKELL
          textColor: text-catppuccin-mocha
          backgroundColor: mauve-catppuccin-latte
          backgroundColorBrightnessModifier: -20
          customFontFilePath: "./resources/fonts/SofiaSans-Black.ttf"
          iconParameters:
            filePath: "./resources/icons/haskell.svg"
        destinationFilePath: "./dist/haskell.png"
        destinationFileExtension: png


<a id="org0339297"></a>

## Using Programatically

Take a look at the [tasks.scala](./resources/examples/tasks.scala) to get an idea of how the utility can be used with plain Scala code.

For advanced usage look at [Main.scala](./src/main/scala/Main.scala) and [HoneyBadger.scala](./src/main/scala/HoneyBadger.scala) .


<a id="org0e23a49"></a>

## Supported formats

As of now, the utility supports exporting badges to: PNG, JPEG, others ?

As of now, the icons to use in the badge can be: SVG, PNG, JPEG, others ?

