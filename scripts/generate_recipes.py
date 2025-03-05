import json

recipes = "src/main/resources/data/mo_glass/recipe"
minecraft_colors = ["white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray", "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black"]

slab_crafting_template = {
	"type": "minecraft:crafting_shaped",
	"category": "building",
	"group": "stained_glass_slab",
	"key": {
		"#": "minecraft:glass"
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
	"ingredient": "minecraft:glass",
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
		"#": "minecraft:glass"
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
	"ingredient": "minecraft:glass",
	"result": {
		"count": 1,
		"id": "mo_glass:glass_stairs"
	}
}

def create_recipes(block, stairs, slab):
	slab_file = f"{recipes}/{slab}.json"
	with open(slab_file, "w") as f:
		recipe = dict(slab_crafting_template)
		recipe["key"]["#"] = f"minecraft:{block}"
		recipe["result"]["id"] = f"mo_glass:{slab}"
		if "stained" not in block:
			del recipe["group"]
		f.write(json.dumps(recipe, indent=2) + "\n")
		print(f"Created {slab_file}")
	slab_from_glass_stonecutting_file = f"{recipes}/{slab}_from_glass_stonecutting.json"
	with open(slab_from_glass_stonecutting_file, "w") as f:
		recipe = dict(slab_from_glass_stonecutting_template)
		recipe["ingredient"] = f"minecraft:{block}"
		recipe["result"]["id"] = f"mo_glass:{slab}"
		f.write(json.dumps(recipe, indent=2) + "\n")
		print(f"Created {slab_from_glass_stonecutting_file}")
	stairs_file = f"{recipes}/{stairs}.json"
	with open(stairs_file, "w") as f:
		recipe = dict(stairs_crafting_template)
		recipe["key"]["#"] = f"minecraft:{block}"
		recipe["result"]["id"] = f"mo_glass:{stairs}"
		if "stained" not in block:
			del recipe["group"]
		f.write(json.dumps(recipe, indent=2) + "\n")
		print(f"Created {stairs_file}")
	stairs_from_glass_stonecutting_file = f"{recipes}/{stairs}_from_glass_stonecutting.json"
	with open(stairs_from_glass_stonecutting_file, "w") as f:
		recipe = dict(stairs_from_glass_stonecutting_template)
		recipe["ingredient"] = f"minecraft:{block}"
		recipe["result"]["id"] = f"mo_glass:{stairs}"
		f.write(json.dumps(recipe, indent=2) + "\n")
		print(f"Created {stairs_from_glass_stonecutting_file}")

create_recipes("glass", "glass_stairs", "glass_slab")
create_recipes("tinted_glass", "tinted_glass_stairs", "tinted_glass_slab")
for color in minecraft_colors:
	create_recipes(f"{color}_stained_glass", f"{color}_stained_glass_stairs", f"{color}_stained_glass_slab")
