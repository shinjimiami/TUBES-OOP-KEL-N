#!/usr/bin/env python3
"""
Generate all placeholder images for Nimonscooked game assets
Run this script to create colored placeholder PNG files for missing assets
"""

from PIL import Image, ImageDraw, ImageFont
import os

def create_placeholder(filepath, width, height, bg_color, text, text_color=(255, 255, 255)):
    """Create a placeholder image with text"""
    # Create directory if it doesn't exist
    os.makedirs(os.path.dirname(filepath), exist_ok=True)
    
    # Skip if file already exists (don't overwrite user's custom images)
    if os.path.exists(filepath):
        print(f"⏭️  Skipped (exists): {filepath}")
        return
    
    # Create image
    img = Image.new('RGBA', (width, height), bg_color)
    draw = ImageDraw.Draw(img)
    
    # Try to use a font, fall back to default if not available
    try:
        font = ImageFont.truetype("/System/Library/Fonts/Helvetica.ttc", 20)
    except:
        font = ImageFont.load_default()
    
    # Draw text in center
    bbox = draw.textbbox((0, 0), text, font=font)
    text_width = bbox[2] - bbox[0]
    text_height = bbox[3] - bbox[1]
    
    x = (width - text_width) // 2
    y = (height - text_height) // 2
    
    # Draw shadow
    draw.text((x+2, y+2), text, fill=(0, 0, 0, 128), font=font)
    # Draw text
    draw.text((x, y), text, fill=text_color, font=font)
    
    # Save
    img.save(filepath)
    print(f"✅ Created: {filepath}")

def main():
    print("=" * 60)
    print("GENERATING PLACEHOLDER IMAGES FOR NIMONSCOOKED")
    print("=" * 60)
    
    base_path = os.path.dirname(os.path.abspath(__file__))
    
    # ===================
    # TILES
    # ===================
    print("\n📐 TILES (Floors & Walls)")
    tiles = [
        ("tiles/floor.png", 64, 64, (200, 200, 200, 255), "FLOOR"),
        ("tiles/wall.png", 64, 64, (100, 100, 100, 255), "WALL"),
        ("tiles/counter.png", 64, 64, (139, 90, 43, 255), "COUNTER"),
    ]
    for filename, w, h, color, text in tiles:
        create_placeholder(os.path.join(base_path, filename), w, h, color, text)
    
    # ===================
    # COOKING STATION STATES (Panci di Kompor)
    # ===================
    print("\n🍳 COOKING STATION - Pan States")
    os.makedirs(os.path.join(base_path, "stations/cooking"), exist_ok=True)
    
    cooking_states = [
        ("stations/cooking/pan_empty.png", "PAN\nEMPTY", (100, 100, 100, 255)),
        
        # Meat in pan
        ("stations/cooking/pan_meat_raw.png", "MEAT\nRAW", (255, 100, 100, 255)),
        ("stations/cooking/pan_meat_cooking.png", "MEAT~", (255, 150, 50, 255)),
        ("stations/cooking/pan_meat_cooked.png", "MEAT+", (139, 69, 19, 255)),
        ("stations/cooking/pan_meat_burned.png", "MEAT X", (50, 50, 50, 255)),
        
        # Cheese in pan
        ("stations/cooking/pan_cheese_raw.png", "CHEESE\nRAW", (255, 255, 100, 255)),
        ("stations/cooking/pan_cheese_cooking.png", "CHEESE~", (255, 200, 50, 255)),
        ("stations/cooking/pan_cheese_cooked.png", "CHEESE+", (255, 165, 0, 255)),
        ("stations/cooking/pan_cheese_burned.png", "CHEESE X", (50, 50, 50, 255)),
        
        # Lettuce in pan
        ("stations/cooking/pan_lettuce_raw.png", "LETTUCE\nRAW", (100, 255, 100, 255)),
        ("stations/cooking/pan_lettuce_cooking.png", "LETTUCE~", (50, 200, 50, 255)),
        ("stations/cooking/pan_lettuce_cooked.png", "LETTUCE+", (34, 139, 34, 255)),
        ("stations/cooking/pan_lettuce_burned.png", "LETTUCE X", (50, 50, 50, 255)),
        
        # Tomato in pan
        ("stations/cooking/pan_tomato_raw.png", "TOMATO\nRAW", (255, 50, 50, 255)),
        ("stations/cooking/pan_tomato_cooking.png", "TOMATO~", (200, 50, 50, 255)),
        ("stations/cooking/pan_tomato_cooked.png", "TOMATO+", (139, 0, 0, 255)),
        ("stations/cooking/pan_tomato_burned.png", "TOMATO X", (50, 50, 50, 255)),
    ]
    
    for filename, text, color in cooking_states:
        create_placeholder(os.path.join(base_path, filename), 128, 128, color, text)
    
    # ===================
    # CUTTING STATION STATES (Ingredient di Talenan)
    # ===================
    print("\n🔪 CUTTING STATION - Board States")
    os.makedirs(os.path.join(base_path, "stations/cutting"), exist_ok=True)
    
    cutting_states = [
        ("stations/cutting/board_empty.png", "BOARD\nEMPTY", (139, 90, 43, 255)),
        
        # Meat on board
        ("stations/cutting/board_meat_raw.png", "MEAT\nRAW", (255, 100, 100, 255)),
        ("stations/cutting/board_meat_chopped.png", "MEAT*", (200, 50, 50, 255)),
        
        # Cheese on board
        ("stations/cutting/board_cheese_raw.png", "CHEESE\nRAW", (255, 255, 100, 255)),
        ("stations/cutting/board_cheese_chopped.png", "CHEESE*", (255, 200, 0, 255)),
        
        # Lettuce on board
        ("stations/cutting/board_lettuce_raw.png", "LETTUCE\nRAW", (100, 255, 100, 255)),
        ("stations/cutting/board_lettuce_chopped.png", "LETTUCE*", (50, 200, 50, 255)),
        
        # Tomato on board
        ("stations/cutting/board_tomato_raw.png", "TOMATO\nRAW", (255, 50, 50, 255)),
        ("stations/cutting/board_tomato_chopped.png", "TOMATO*", (200, 0, 0, 255)),
    ]
    
    for filename, text, color in cutting_states:
        create_placeholder(os.path.join(base_path, filename), 128, 128, color, text)
    
    # ===================
    # ASSEMBLY STATION STATES (Burger Assembly)
    # ===================
    print("\n🍔 ASSEMBLY STATION - Burger States")
    os.makedirs(os.path.join(base_path, "stations/assembly"), exist_ok=True)
    
    assembly_states = [
        ("stations/assembly/plate_empty.png", "PLATE\nEMPTY", (255, 255, 255, 255)),
        ("stations/assembly/plate_bun.png", "BUN", (255, 220, 160, 255)),
        ("stations/assembly/plate_bun_meat.png", "BUN+\nMEAT", (200, 100, 50, 255)),
        ("stations/assembly/plate_bun_meat_cheese.png", "BUN+MEAT\n+CHEESE", (255, 180, 50, 255)),
        ("stations/assembly/plate_bun_meat_lettuce.png", "BUN+MEAT\n+LETTUCE", (150, 200, 100, 255)),
        ("stations/assembly/plate_bun_meat_tomato.png", "BUN+MEAT\n+TOMATO", (200, 100, 100, 255)),
        ("stations/assembly/plate_classic.png", "CLASSIC\nBURGER", (139, 69, 19, 255)),
        ("stations/assembly/plate_cheeseburger.png", "CHEESE\nBURGER", (255, 165, 0, 255)),
        ("stations/assembly/plate_blt.png", "BLT\nBURGER", (150, 200, 50, 255)),
        ("stations/assembly/plate_deluxe.png", "DELUXE\nBURGER", (255, 140, 50, 255)),
    ]
    
    for filename, text, color in assembly_states:
        create_placeholder(os.path.join(base_path, filename), 128, 128, color, text)
    
    # ===================
    # MENU BACKGROUNDS
    # ===================
    print("\n📱 MENU BACKGROUNDS")
    menu_assets = [
        ("menu/how_to_play.png", 1024, 768, (50, 100, 150, 255), "HOW TO PLAY\n\nReplace with your image"),
        ("menu/stage_select.png", 1024, 768, (100, 50, 150, 255), "STAGE SELECT\n\nReplace with your image"),
        ("menu/win_screen.png", 1024, 768, (50, 200, 50, 255), "YOU WIN!\n\nReplace with your image"),
        ("menu/lose_screen.png", 1024, 768, (200, 50, 50, 255), "YOU LOSE!\n\nReplace with your image"),
    ]
    
    for filename, w, h, color, text in menu_assets:
        create_placeholder(os.path.join(base_path, filename), w, h, color, text)
    
    # ===================
    # UI ELEMENTS
    # ===================
    print("\n🎨 UI ELEMENTS")
    os.makedirs(os.path.join(base_path, "ui"), exist_ok=True)
    
    ui_assets = [
        ("ui/order_ticket.png", 300, 150, (255, 255, 200, 255), "ORDER\nTICKET"),
        ("ui/timer_bar.png", 200, 30, (50, 150, 255, 255), "TIMER BAR"),
        ("ui/score_panel.png", 250, 100, (50, 50, 50, 200), "SCORE\nPANEL"),
        ("ui/chef_indicator.png", 64, 64, (255, 215, 0, 255), "ACTIVE"),
    ]
    
    for filename, w, h, color, text in ui_assets:
        create_placeholder(os.path.join(base_path, filename), w, h, color, text)
    
    # ===================
    # FLOOR ITEMS INDICATOR (Optional)
    # ===================
    print("\n⭕ FLOOR ITEMS")
    os.makedirs(os.path.join(base_path, "items/floor"), exist_ok=True)
    
    # Create a circular indicator
    filepath = os.path.join(base_path, "items/floor/indicator_circle.png")
    if not os.path.exists(filepath):
        img = Image.new('RGBA', (64, 64), (0, 0, 0, 0))
        draw = ImageDraw.Draw(img)
        draw.ellipse([2, 2, 62, 62], fill=(255, 255, 255, 100), outline=(255, 255, 255, 255), width=3)
        img.save(filepath)
        print(f"✅ Created: {filepath}")
    else:
        print(f"⏭️  Skipped (exists): {filepath}")
    
    print("\n" + "=" * 60)
    print("✨ PLACEHOLDER GENERATION COMPLETE!")
    print("=" * 60)
    print("\n📝 Next Steps:")
    print("1. Check PLACEHOLDER_LIST.md for full asset list")
    print("2. Replace placeholder images with your custom artwork")
    print("3. Keep the same filename and dimensions when replacing")
    print("4. Priority: Tiles, Cooking/Cutting states, Menu backgrounds")
    print("\n⚠️  Note: Existing images were NOT overwritten")
    print("=" * 60)

if __name__ == "__main__":
    main()
