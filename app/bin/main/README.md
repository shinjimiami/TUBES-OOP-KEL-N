# Resources Directory

This directory contains all image assets for the Nimonscooked game.

## Required Image Files

All images should be PNG format, recommended size: 48x48 pixels.

### Stations (`/stations/`)
- `assembly_station.png` - Assembly station sprite
- `cooking_station.png` - Cooking station sprite  
- `cutting_station.png` - Cutting station sprite
- `ingredient_storage.png` - Ingredient storage sprite
- `plate_storage.png` - Plate storage sprite
- `serving_counter.png` - Serving counter sprite
- `washing_station.png` - Washing station sprite

### Kitchen Utensils (`/items/kitchen_utensils/`)
- `plate.png` - Clean plate sprite
- `frying_pan.png` - Frying pan sprite

### Ingredients (`/items/ingredients/`)
Each ingredient needs 5 state images:
- `<ingredient>_raw.png` - Raw state
- `<ingredient>_chopped.png` - Chopped state
- `<ingredient>_cooking.png` - Cooking state
- `<ingredient>_cooked.png` - Cooked state
- `<ingredient>_burned.png` - Burned state

Required ingredients:
- **bun** (bun_raw.png, bun_chopped.png, etc.)
- **cheese** (cheese_raw.png, cheese_chopped.png, etc.)
- **lettuce** (lettuce_raw.png, lettuce_chopped.png, etc.)
- **meat** (meat_raw.png, meat_chopped.png, etc.)
- **tomato** (tomato_raw.png, tomato_chopped.png, etc.)

### Chef Sprites (`/chef/`)
- Chef character sprites (to be implemented)

### Tiles (`/tiles/`)
- Map tile sprites (to be implemented)

## Note
If image files are missing, the game will still run but entities will not display sprites. The Entity class has null-safe image loading that returns null if resources are not found.
