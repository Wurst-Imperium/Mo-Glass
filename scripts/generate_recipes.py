import json
from pathlib import Path
from util import minecraft_colors


def write_recipe_file(file_name, recipe_data):
	recipe_path = Path("src") / "main" / "resources" / "data" / "mo_glass" / "recipe" / file_name
	recipe_path.parent.mkdir(parents=True, exist_ok=True)
	recipe_path.write_text(json.dumps(recipe_data, indent=2) + "\n")
	print(f"Created {recipe_path}")


def create_slab_crafting_recipe(block, slab):
	recipe = {
		"type": "minecraft:crafting_shaped",
		"category": "building",
		"group": "stained_glass_slab",
		"key": {"#": "minecraft:glass"},
		"pattern": ["###"],
		"result": {"count": 6, "id": "mo_glass:glass_slab"},
	}
	recipe["key"]["#"] = f"minecraft:{block}"
	recipe["result"]["id"] = f"mo_glass:{slab}"
	if "stained" not in block:
		del recipe["group"]
	write_recipe_file(f"{slab}.json", recipe)


def create_stairs_crafting_recipe(block, stairs):
	recipe = {
		"type": "minecraft:crafting_shaped",
		"category": "building",
		"group": "stained_glass_stairs",
		"key": {"#": "minecraft:glass"},
		"pattern": ["#  ", "## ", "###"],
		"result": {"count": 4, "id": "mo_glass:glass_stairs"},
	}
	recipe["key"]["#"] = f"minecraft:{block}"
	recipe["result"]["id"] = f"mo_glass:{stairs}"
	if "stained" not in block:
		del recipe["group"]
	write_recipe_file(f"{stairs}.json", recipe)


def create_slab_stonecutting_recipe(block, slab):
	recipe = {
		"type": "minecraft:stonecutting",
		"ingredient": "minecraft:glass",
		"result": {"count": 2, "id": "mo_glass:glass_slab"},
	}
	recipe["ingredient"] = f"minecraft:{block}"
	recipe["result"]["id"] = f"mo_glass:{slab}"
	write_recipe_file(f"{slab}_from_glass_stonecutting.json", recipe)


def create_stairs_stonecutting_recipe(block, stairs):
	recipe = {
		"type": "minecraft:stonecutting",
		"ingredient": "minecraft:glass",
		"result": {"count": 1, "id": "mo_glass:glass_stairs"},
	}
	recipe["ingredient"] = f"minecraft:{block}"
	recipe["result"]["id"] = f"mo_glass:{stairs}"
	write_recipe_file(f"{stairs}_from_glass_stonecutting.json", recipe)


def create_recipes(block, stairs, slab):
	create_slab_crafting_recipe(block, slab)
	create_stairs_crafting_recipe(block, stairs)
	create_slab_stonecutting_recipe(block, slab)
	create_stairs_stonecutting_recipe(block, stairs)


def main():
	create_recipes("glass", "glass_stairs", "glass_slab")
	create_recipes("tinted_glass", "tinted_glass_stairs", "tinted_glass_slab")
	for color in minecraft_colors:
		create_recipes(
			f"{color}_stained_glass", f"{color}_stained_glass_stairs", f"{color}_stained_glass_slab"
		)


if __name__ == "__main__":
	main()
