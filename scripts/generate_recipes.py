# To generate recipes, place this script in the data/mo_glass/recipes folder and run it.

import os
import json

cwd = os.path.dirname(os.path.realpath(__file__))
minecraft_colors = ["white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray", "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black"]

slab_crafting_template = {
	"type": "minecraft:crafting_shaped",
	"category": "building",
	"group": "stained_glass_slab",
	"key": {
		"#": {
			"item": "minecraft:glass"
		}
	},
	"pattern": [
		"###"
	],
	"result": {
		"count": 6,
		"id": "mo_glass:glass_slab"
	}
}

slab_from_glass_stonecutting_template = {
	"type": "minecraft:stonecutting",
	"ingredient": {
		"item": "minecraft:glass"
	},
	"result": {
		"count": 2,
		"id": "mo_glass:glass_slab"
	}
}

stairs_crafting_template = {
	"type": "minecraft:crafting_shaped",
	"category": "building",
	"group": "stained_glass_stairs",
	"key": {
		"#": {
			"item": "minecraft:glass"
		}
	},
	"pattern": [
		"#  ",
		"## ",
		"###"
	],
	"result": {
		"count": 4,
		"id": "mo_glass:glass_stairs"
	}
}

stairs_from_glass_stonecutting_template = {
	"type": "minecraft:stonecutting",
	"ingredient": {
		"item": "minecraft:glass"
	},
	"result": {
		"count": 1,
		"id": "mo_glass:glass_stairs"
	}
}

def create_recipes(block, stairs, slab):
	slab_file = f"{cwd}/{slab}.json"
	with open(slab_file, "w") as f:
		recipe = dict(slab_crafting_template)
		recipe["key"]["#"]["item"] = f"minecraft:{block}"
		recipe["result"]["id"] = f"mo_glass:{slab}"
		if not "stained" in block:
			del recipe["group"]
		json.dump(recipe, f, indent=2)
		print(f"Created {slab_file}")
	slab_from_glass_stonecutting_file = f"{cwd}/{slab}_from_glass_stonecutting.json"
	with open(slab_from_glass_stonecutting_file, "w") as f:
		recipe = dict(slab_from_glass_stonecutting_template)
		recipe["ingredient"]["item"] = f"minecraft:{block}"
		recipe["result"]["id"] = f"mo_glass:{slab}"
		json.dump(recipe, f, indent=2)
		print(f"Created {slab_from_glass_stonecutting_file}")
	stairs_file = f"{cwd}/{stairs}.json"
	with open(stairs_file, "w") as f:
		recipe = dict(stairs_crafting_template)
		recipe["key"]["#"]["item"] = f"minecraft:{block}"
		recipe["result"]["id"] = f"mo_glass:{stairs}"
		if not "stained" in block:
			del recipe["group"]
		json.dump(recipe, f, indent=2)
		print(f"Created {stairs_file}")
	stairs_from_glass_stonecutting_file = f"{cwd}/{stairs}_from_glass_stonecutting.json"
	with open(stairs_from_glass_stonecutting_file, "w") as f:
		recipe = dict(stairs_from_glass_stonecutting_template)
		recipe["ingredient"]["item"] = f"minecraft:{block}"
		recipe["result"]["id"] = f"mo_glass:{stairs}"
		json.dump(recipe, f, indent=2)
		print(f"Created {stairs_from_glass_stonecutting_file}")

create_recipes("glass", "glass_stairs", "glass_slab")
create_recipes("tinted_glass", "tinted_glass_stairs", "tinted_glass_slab")
for color in minecraft_colors:
	create_recipes(f"{color}_stained_glass", f"{color}_stained_glass_stairs", f"{color}_stained_glass_slab")
input("Done! Press enter to exit.")
