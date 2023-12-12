# Mo Glass

Mo Glass is a Minecraft mod that adds glass stairs and glass slabs, including stained and tinted glass variants. They look great in any build and connect seamlessly with vanilla glass blocks. The mod is available for many different Minecraft versions, including snapshots.

![A Minecraft house with its roof made out of glass stairs, powered by the Mo Glass mod](https://user-images.githubusercontent.com/10100202/69939492-ab78a480-14e8-11ea-8aa7-c351657b334b.jpg)

## Downloads (for users)

[![Download Mo Glass](https://user-images.githubusercontent.com/10100202/214880552-859aa2ed-b4bc-4f8d-9ee7-bdd8c7fb33a2.png)](https://www.wimods.net/mo-glass/download/?utm_source=GitHub&utm_medium=Mo+Glass&utm_content=Mo+Glass+GitHub+repo+download+button)

## Setup (for developers)

(This assumes that you are using Windows with [Eclipse](https://www.eclipse.org/downloads/) and [Java Development Kit 17](https://adoptium.net/?variant=openjdk17&jvmVariant=hotspot) already installed.)

1. Clone / download the repository.

2. Run these two commands in PowerShell:

   ```powershell
   ./gradlew.bat --stop
   ./gradlew.bat genEclipseRuns eclipse
   ```

3. In Eclipse, go to `Import...` > `Existing Projects into Workspace` and select this project.

## Features

- Glass Slabs!
- Glass Stairs!
- Stained Glass Slabs! (since v1.3)
- Stained Glass Stairs! (since v1.3)
- Tinted Glass Slabs! (since v1.6)
- Tinted Glass Stairs! (since v1.6)
- Working Transparency! (see below)
- Working Translucency! (for tinted glass)

## Seamless Transparency

Both the stairs and the slabs are see-through in the same way as vanilla glass blocks. You can place multiple stairs, slabs and full blocks next to each other and they will look like a single piece of glass.

![an example of seamless transparency in Mo Glass, annotated with arrows to show where the glass pieces connect](https://user-images.githubusercontent.com/10100202/69958444-821e3f80-150d-11ea-8f89-b241c66a8849.jpg)

## Working Translucency

Slabs and stairs made from tinted glass will block light if you place them without any air gaps, or let the light through if there is a gap.

![an example of translucency in Mo Glass, demonstrated with tinted glass stairs that either block light or let it through depending on their orientation](https://user-images.githubusercontent.com/10100202/145865191-04baa767-39f8-445d-8ea1-7e08619bb975.jpg)

## Why aren't these blocks part of vanilla Minecraft?

Before I started making this mod, I always thought that Mojang just forgot about these blocks, or that they were too lazy to add them. But now that I've added these blocks myself and spent many hours to get the transparency to work, I think I finally understand why Mojang didn't bother. Minecraft's stairs are surprisingly complex blocks and they were never meant to be made transparent. Slabs might be easy enough, but I think if Mojang added those then people wouldn't stop asking for the stairs. And glass stairs are an absolute nightmare to make.

Stairs can be placed facing North, East, South or West, they can be upside-down or right-side-up, they can be straight or curved in one of four different ways and they have six faces that may or may not be transparent, depending on what block is next to them. But here's the catch: On each of those six faces, there could be another block of glass stairs that can also be placed facing North, East, South or West, upside-down or right-side-up and either straight or curved in one of four different ways. And then of course, on each of the six faces there could also be a non-transparent block, a regular glass block or a glass slab that is either placed on the bottom half, top half, or is a double slab.

In the end, there are 10800 possible scenarios that need to be accounted for just to calculate transparency of glass stairs. But then you also have to calculate the transparency of glass slabs (810 possible scenarios) and adjust the transparency calculation of regular glass blocks (270 possible scenarios, or 258 more than before).*

That's a lot of effort just to add two new blocks to the game - and a lot of opportunities for new bugs to sneak in. That, I think, is why Mojang didn't bother.

<details>
  <summary>*Here's how I got those numbers: (click to expand)</summary>
  
  possible variations of stairs:
  pvStairs = 4 * 2 * 5 = 40
  
  possible variations of slabs:
  pvSlabs = 3
  
  possible variations of glass blocks:
  pvGlass = 1
  
  possible variations of non-transparent blocks:
  pvBlocks = 1 (because any variations would be ignored when calculating transparency)
  
  possible combinations combined:
  pvAll = pvStairs + pvSlabs + pvGlass + pvBlocks&nbsp;= 40 + 3 + 1 + 1 = 45
  
  possibly transparent faces of a block (including stairs, even though they have more faces):
  f = 6
  
  possible scenarios for transparency of stairs:
  psStairs = pvAll * f * pvStairs = 45 * 6 * 40 = 10800
  
  possible scenarios for transparency of slabs:
  psSlabs = pvAll * f * pvSlabs = 45 * 6 * 3 = 810
  
  possible scenarios for transparency of glass blocks:
  psGlass = pvAll * f * pvGlass = 45 * 6 * 1 = 270
  
  possible scenarios for transparency of glass blocks if glass stairs and slabs don't exist:
  psGlassVanilla = (pvGlass + pvBlocks) * f * pvGlass = (1 + 1) * 6 * 1 = 12&nbsp;
</details>

## Crafting Recipes

<details>
  <summary>Glass Slab: (click to expand)</summary>
  
  ![glass slab crafting recipse](https://user-images.githubusercontent.com/10100202/69957444-5a2ddc80-150b-11ea-8c8c-e2afc5d72fb7.png)  
  ![glass slab stonecutter recipe](https://user-images.githubusercontent.com/10100202/70445670-2a974b00-1a9c-11ea-9a09-46c304cd167b.png)
</details>

<details>
  <summary>Glass Stairs: (click to expand)</summary>
  
  ![glass stairs crafting recipe](https://user-images.githubusercontent.com/10100202/69957446-5bf7a000-150b-11ea-8e61-d189de63333d.png)  
  ![glass stairs stonecutter recipe](https://user-images.githubusercontent.com/10100202/70445677-2c610e80-1a9c-11ea-8e1b-108863b47124.png)
</details>

## Supported Languages

- Chinese (Simplified) (since v1.2)
- Chinese (Traditional) (since v1.2)
- English (US) (since v1.0)
- French (France) (since v1.6)
- German (Germany) (since v1.2)
- Italian (Italy) (since v1.6)
- Japanese (Japan) (since v1.7)
- Oshiwambo (Oshindonga) (since v1.5)
- Oshiwambo (Oshikwanyama) (since v1.5)
- Portuguese (Brazil) (since v1.7)
- Russian (Russia) (since v1.5)
- Spanish (Argentina) (since v1.4)
- Spanish (Chile) (since v1.4)
- Spanish (Ecuador) (since v1.4)
- Spanish (Spain) (since v1.4)
- Spanish (Mexico) (since v1.4)
- Spanish (Uruguay) (since v1.4)
- Spanish (Venezuela) (since v1.4)
