![](https://user-images.githubusercontent.com/10100202/69939492-ab78a480-14e8-11ea-8aa7-c351657b334b.jpg)

# Mo Glass

Adds glass stairs and glass slabs to Minecraft.

## Features

- Glass Slabs!
- Glass Stairs!
- Working Transparency! (see below)

## Seamless Transparency

Both the stairs and the slabs are see-through in the same way as vanilla glass blocks. You can place multiple stairs, slabs and full blocks next to each other and they will look like a single piece of glass.

![](https://user-images.githubusercontent.com/10100202/69958444-821e3f80-150d-11ea-8f89-b241c66a8849.jpg)

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

## Downloads

https://www.curseforge.com/minecraft/mc-mods/mo-glass

## Setup For Developers (using Windows 10 and Eclipse)

1. Clone / download the repository.

2. Run these two commands in PowerShell:

   ```powershell
   ./gradlew.bat genSources
   ./gradlew.bat eclipse
   ```

3. In Eclipse, go to `Import...` > `Existing Projects into Workspace` and select this project.
