#!/usr/bin/env python3
"""
Generate placeholder PNG images for Nimonscooked game resources.
Requires: pip install pillow
"""

from PIL import Image, ImageDraw, ImageFont
import os

SIZE = 48
COLORS = {
    # Stations
    'assembly_station': '#8B4513',
    'cooking_station': '#FF4500',
    'cutting_station': '#4682B4',
    'ingredient_storage': '#32CD32',
    'plate_storage': '#FFD700',
    'serving_counter': '#9370DB',
    'washing_station': '#00CED1',
    
    # Kitchen utensils
    'plate': '#E0E0E0',
    'frying_pan': '#2F4F4F',
    
    # Ingredients states
    'raw': '#FFB6C1',
    'chopped': '#FF69B4',
    'cooking': '#FFA500',
    'cooked': '#8B4513',
    'burned': '#000000',
}

def create_placeholder(path, color, label):
    """Create a simple colored square with text label"""
    img = Image.new('RGB', (SIZE, SIZE), color=color)
    draw = ImageDraw.Draw(img)
    
    # Draw border
    draw.rectangle([0, 0, SIZE-1, SIZE-1], outline='#FFFFFF', width=2)
    
    # Add text label (small)
    try:
        font = ImageFont.truetype("/System/Library/Fonts/Helvetica.ttc", 8)
    except:
        font = ImageFont.load_default()
    
    # Center text
    text = label[:8]  # Truncate long names
    bbox = draw.textbbox((0, 0), text, font=font)
    text_width = bbox[2] - bbox[0]
    text_height = bbox[3] - bbox[1]
    x = (SIZE - text_width) // 2
    y = (SIZE - text_height) // 2
    
    draw.text((x, y), text, fill='#FFFFFF', font=font)
    
    # Create directory if needed
    os.makedirs(os.path.dirname(path), exist_ok=True)
    img.save(path)
    print(f"Created: {path}")

def main():
    base_path = os.path.dirname(__file__)
    
    # Stations
    stations = ['assembly_station', 'cooking_station', 'cutting_station', 
                'ingredient_storage', 'plate_storage', 'serving_counter', 'washing_station']
    for station in stations:
        path = os.path.join(base_path, 'stations', f'{station}.png')
        create_placeholder(path, COLORS[station], station.split('_')[0])
    
    # Kitchen utensils
    utensils = ['plate', 'frying_pan']
    for utensil in utensils:
        path = os.path.join(base_path, 'items', 'kitchen_utensils', f'{utensil}.png')
        create_placeholder(path, COLORS[utensil], utensil)
    
    # Ingredients with states
    ingredients = ['bun', 'cheese', 'lettuce', 'meat', 'tomato']
    states = ['raw', 'chopped', 'cooking', 'cooked', 'burned']
    
    for ingredient in ingredients:
        for state in states:
            path = os.path.join(base_path, 'items', 'ingredients', f'{ingredient}_{state}.png')
            color = COLORS[state]
            label = ingredient[:3].upper()  # First 3 letters
            create_placeholder(path, color, label)
    
    print("\nAll placeholder images generated successfully!")
    print("You can replace these with actual game sprites later.")

if __name__ == '__main__':
    main()
