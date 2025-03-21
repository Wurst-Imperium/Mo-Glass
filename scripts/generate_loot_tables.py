import json
from pathlib import Path
from util import minecraft_colors


def write_loot_table_file(file_name, loot_table_data):
	file_path = (
		Path("src")
		/ "main"
		/ "resources"
		/ "data"
		/ "mo_glass"
		/ "loot_table"
		/ "blocks"
		/ file_name
	)
	file_path.parent.mkdir(parents=True, exist_ok=True)
	file_path.write_text(json.dumps(loot_table_data, indent=2) + "\n")
	print(f"Created {file_path}")


def create_slab_loot_table(slab, requires_silk_touch=True):
	data = {
		"type": "minecraft:block",
		"pools": [
			{
				"bonus_rolls": 0.0,
				"conditions": [
					{
						"condition": "minecraft:match_tool",
						"predicate": {
							"predicates": {
								"minecraft:enchantments": [
									{"enchantments": "minecraft:silk_touch", "levels": {"min": 1}}
								]
							}
						},
					}
				],
				"entries": [
					{
						"type": "minecraft:item",
						"functions": [
							{
								"add": False,
								"conditions": [
									{
										"block": f"mo_glass:{slab}",
										"condition": "minecraft:block_state_property",
										"properties": {"type": "double"},
									}
								],
								"count": 2.0,
								"function": "minecraft:set_count",
							},
							{"function": "minecraft:explosion_decay"},
						],
						"name": f"mo_glass:{slab}",
					}
				],
				"rolls": 1.0,
			}
		],
		"random_sequence": f"mo_glass:blocks/{slab}",
	}
	if not requires_silk_touch:
		data["pools"][0]["conditions"] = [{"condition": "minecraft:survives_explosion"}]
	write_loot_table_file(f"{slab}.json", data)


def create_stairs_loot_table(stairs, requires_silk_touch=True):
	data = {
		"type": "minecraft:block",
		"pools": [
			{
				"bonus_rolls": 0.0,
				"conditions": [
					{
						"condition": "minecraft:match_tool",
						"predicate": {
							"predicates": {
								"minecraft:enchantments": [
									{"enchantments": "minecraft:silk_touch", "levels": {"min": 1}}
								]
							}
						},
					}
				],
				"entries": [{"type": "minecraft:item", "name": f"mo_glass:{stairs}"}],
				"rolls": 1.0,
			}
		],
		"random_sequence": f"mo_glass:blocks/{stairs}",
	}
	if not requires_silk_touch:
		data["pools"][0]["conditions"] = [{"condition": "minecraft:survives_explosion"}]
	write_loot_table_file(f"{stairs}.json", data)


def create_loot_tables(stairs, slab, requires_silk_touch=True):
	create_slab_loot_table(slab, requires_silk_touch)
	create_stairs_loot_table(stairs, requires_silk_touch)


def main():
	create_loot_tables("glass_stairs", "glass_slab")
	create_loot_tables("tinted_glass_stairs", "tinted_glass_slab", False)
	for color in minecraft_colors:
		create_loot_tables(f"{color}_stained_glass_stairs", f"{color}_stained_glass_slab")


if __name__ == "__main__":
	main()
