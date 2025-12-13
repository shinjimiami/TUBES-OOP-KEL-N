#!/bin/bash
# Generate placeholder images without requiring PIL
# Creates simple colored rectangles using ImageMagick or base64 encoded PNGs

BASE_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Function to create a simple colored PNG placeholder
create_placeholder() {
    local filepath="$1"
    local width="$2"
    local height="$3"
    local color="$4"
    local text="$5"
    
    # Create directory if needed
    mkdir -p "$(dirname "$filepath")"
    
    # Skip if exists
    if [ -f "$filepath" ]; then
        echo "⏭️  Skipped: $filepath"
        return
    fi
    
    # Check if ImageMagick is available
    if command -v convert &> /dev/null; then
        convert -size "${width}x${height}" "xc:${color}" \
                -gravity center -pointsize 20 -fill white \
                -annotate +0+0 "$text" \
                "$filepath"
        echo "✅ Created: $filepath"
    else
        # Create a minimal valid PNG (1x1 pixel) as fallback
        # This is a base64 encoded 1x1 transparent PNG
        echo "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg==" | base64 -d > "$filepath"
        echo "⚠️  Created minimal: $filepath (ImageMagick not found)"
    fi
}

echo "============================================================"
echo "GENERATING PLACEHOLDER IMAGES FOR NIMONSCOOKED"
echo "============================================================"

# TILES
echo ""
echo "📐 TILES"
create_placeholder "$BASE_DIR/tiles/floor.png" 64 64 "gray" "FLOOR"
create_placeholder "$BASE_DIR/tiles/wall.png" 64 64 "darkgray" "WALL"
create_placeholder "$BASE_DIR/tiles/counter.png" 64 64 "brown" "COUNTER"

# COOKING STATION STATES
echo ""
echo "🍳 COOKING STATION - Pan States"
mkdir -p "$BASE_DIR/stations/cooking"

create_placeholder "$BASE_DIR/stations/cooking/pan_empty.png" 128 128 "gray" "PAN EMPTY"
create_placeholder "$BASE_DIR/stations/cooking/pan_meat_raw.png" 128 128 "red" "MEAT RAW"
create_placeholder "$BASE_DIR/stations/cooking/pan_meat_cooking.png" 128 128 "orange" "MEAT~"
create_placeholder "$BASE_DIR/stations/cooking/pan_meat_cooked.png" 128 128 "brown" "MEAT+"
create_placeholder "$BASE_DIR/stations/cooking/pan_meat_burned.png" 128 128 "black" "MEAT X"

create_placeholder "$BASE_DIR/stations/cooking/pan_cheese_raw.png" 128 128 "yellow" "CHEESE RAW"
create_placeholder "$BASE_DIR/stations/cooking/pan_cheese_cooking.png" 128 128 "gold" "CHEESE~"
create_placeholder "$BASE_DIR/stations/cooking/pan_cheese_cooked.png" 128 128 "orange" "CHEESE+"
create_placeholder "$BASE_DIR/stations/cooking/pan_cheese_burned.png" 128 128 "black" "CHEESE X"

create_placeholder "$BASE_DIR/stations/cooking/pan_lettuce_raw.png" 128 128 "lime" "LETTUCE RAW"
create_placeholder "$BASE_DIR/stations/cooking/pan_lettuce_cooking.png" 128 128 "green" "LETTUCE~"
create_placeholder "$BASE_DIR/stations/cooking/pan_lettuce_cooked.png" 128 128 "darkgreen" "LETTUCE+"
create_placeholder "$BASE_DIR/stations/cooking/pan_lettuce_burned.png" 128 128 "black" "LETTUCE X"

create_placeholder "$BASE_DIR/stations/cooking/pan_tomato_raw.png" 128 128 "tomato" "TOMATO RAW"
create_placeholder "$BASE_DIR/stations/cooking/pan_tomato_cooking.png" 128 128 "red" "TOMATO~"
create_placeholder "$BASE_DIR/stations/cooking/pan_tomato_cooked.png" 128 128 "darkred" "TOMATO+"
create_placeholder "$BASE_DIR/stations/cooking/pan_tomato_burned.png" 128 128 "black" "TOMATO X"

# CUTTING STATION STATES
echo ""
echo "🔪 CUTTING STATION - Board States"
mkdir -p "$BASE_DIR/stations/cutting"

create_placeholder "$BASE_DIR/stations/cutting/board_empty.png" 128 128 "brown" "BOARD EMPTY"
create_placeholder "$BASE_DIR/stations/cutting/board_meat_raw.png" 128 128 "red" "MEAT RAW"
create_placeholder "$BASE_DIR/stations/cutting/board_meat_chopped.png" 128 128 "darkred" "MEAT*"
create_placeholder "$BASE_DIR/stations/cutting/board_cheese_raw.png" 128 128 "yellow" "CHEESE RAW"
create_placeholder "$BASE_DIR/stations/cutting/board_cheese_chopped.png" 128 128 "gold" "CHEESE*"
create_placeholder "$BASE_DIR/stations/cutting/board_lettuce_raw.png" 128 128 "lime" "LETTUCE RAW"
create_placeholder "$BASE_DIR/stations/cutting/board_lettuce_chopped.png" 128 128 "green" "LETTUCE*"
create_placeholder "$BASE_DIR/stations/cutting/board_tomato_raw.png" 128 128 "tomato" "TOMATO RAW"
create_placeholder "$BASE_DIR/stations/cutting/board_tomato_chopped.png" 128 128 "red" "TOMATO*"

# ASSEMBLY STATION STATES
echo ""
echo "🍔 ASSEMBLY STATION - Burger States"
mkdir -p "$BASE_DIR/stations/assembly"

create_placeholder "$BASE_DIR/stations/assembly/plate_empty.png" 128 128 "white" "PLATE"
create_placeholder "$BASE_DIR/stations/assembly/plate_bun.png" 128 128 "wheat" "BUN"
create_placeholder "$BASE_DIR/stations/assembly/plate_bun_meat.png" 128 128 "sienna" "BUN+MEAT"
create_placeholder "$BASE_DIR/stations/assembly/plate_bun_meat_cheese.png" 128 128 "goldenrod" "CHEESE"
create_placeholder "$BASE_DIR/stations/assembly/plate_bun_meat_lettuce.png" 128 128 "yellowgreen" "LETTUCE"
create_placeholder "$BASE_DIR/stations/assembly/plate_bun_meat_tomato.png" 128 128 "coral" "TOMATO"
create_placeholder "$BASE_DIR/stations/assembly/plate_classic.png" 128 128 "brown" "CLASSIC"
create_placeholder "$BASE_DIR/stations/assembly/plate_cheeseburger.png" 128 128 "orange" "CHEESE"
create_placeholder "$BASE_DIR/stations/assembly/plate_blt.png" 128 128 "olivedrab" "BLT"
create_placeholder "$BASE_DIR/stations/assembly/plate_deluxe.png" 128 128 "chocolate" "DELUXE"

# MENU BACKGROUNDS
echo ""
echo "📱 MENU BACKGROUNDS"
create_placeholder "$BASE_DIR/menu/how_to_play.png" 1024 768 "steelblue" "HOW TO PLAY"
create_placeholder "$BASE_DIR/menu/stage_select.png" 1024 768 "mediumpurple" "STAGE SELECT"
create_placeholder "$BASE_DIR/menu/win_screen.png" 1024 768 "limegreen" "YOU WIN"
create_placeholder "$BASE_DIR/menu/lose_screen.png" 1024 768 "crimson" "YOU LOSE"

# UI ELEMENTS
echo ""
echo "🎨 UI ELEMENTS"
mkdir -p "$BASE_DIR/ui"

create_placeholder "$BASE_DIR/ui/order_ticket.png" 300 150 "lightyellow" "ORDER"
create_placeholder "$BASE_DIR/ui/timer_bar.png" 200 30 "skyblue" "TIMER"
create_placeholder "$BASE_DIR/ui/score_panel.png" 250 100 "dimgray" "SCORE"
create_placeholder "$BASE_DIR/ui/chef_indicator.png" 64 64 "gold" "ACTIVE"

# FLOOR ITEMS INDICATOR
echo ""
echo "⭕ FLOOR ITEMS"
mkdir -p "$BASE_DIR/items/floor"
create_placeholder "$BASE_DIR/items/floor/indicator_circle.png" 64 64 "none" ""

echo ""
echo "============================================================"
echo "✨ PLACEHOLDER GENERATION COMPLETE!"
echo "============================================================"
echo ""
echo "📝 Next Steps:"
echo "1. Check PLACEHOLDER_LIST.md for full asset list"
echo "2. Replace placeholder images with your custom artwork"
echo "3. Keep the same filename when replacing"
echo "4. Priority: Cooking/Cutting states, Menu backgrounds"
echo ""
if ! command -v convert &> /dev/null; then
    echo "⚠️  ImageMagick not found - created minimal placeholders"
    echo "💡 Install ImageMagick for better placeholders:"
    echo "   brew install imagemagick"
    echo ""
fi
echo "============================================================"
