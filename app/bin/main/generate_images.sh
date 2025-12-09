#!/bin/bash
# Generate simple placeholder images using ImageMagick (if available)
# or create simple text files as placeholders

cd "$(dirname "$0")"

# Check if ImageMagick is installed
if command -v convert &> /dev/null; then
    echo "Using ImageMagick to generate placeholder images..."
    
    # Stations
    mkdir -p stations
    convert -size 48x48 xc:'#8B4513' -gravity center -pointsize 12 -fill white -annotate +0+0 'ASM' stations/assembly_station.png
    convert -size 48x48 xc:'#FF4500' -gravity center -pointsize 12 -fill white -annotate +0+0 'COOK' stations/cooking_station.png
    convert -size 48x48 xc:'#4682B4' -gravity center -pointsize 12 -fill white -annotate +0+0 'CUT' stations/cutting_station.png
    convert -size 48x48 xc:'#32CD32' -gravity center -pointsize 12 -fill white -annotate +0+0 'STOR' stations/ingredient_storage.png
    convert -size 48x48 xc:'#FFD700' -gravity center -pointsize 12 -fill white -annotate +0+0 'PLAT' stations/plate_storage.png
    convert -size 48x48 xc:'#9370DB' -gravity center -pointsize 12 -fill white -annotate +0+0 'SERV' stations/serving_counter.png
    convert -size 48x48 xc:'#00CED1' -gravity center -pointsize 12 -fill white -annotate +0+0 'WASH' stations/washing_station.png
    
    # Kitchen utensils
    mkdir -p items/kitchen_utensils
    convert -size 48x48 xc:'#E0E0E0' -gravity center -pointsize 12 -fill black -annotate +0+0 'PLATE' items/kitchen_utensils/plate.png
    convert -size 48x48 xc:'#2F4F4F' -gravity center -pointsize 12 -fill white -annotate +0+0 'PAN' items/kitchen_utensils/frying_pan.png
    
    # Ingredients with states
    mkdir -p items/ingredients
    for ingredient in bun cheese lettuce meat tomato; do
        # Get first 3 letters uppercase
        label=$(echo "$ingredient" | cut -c1-3 | tr '[:lower:]' '[:upper:]')
        
        convert -size 48x48 xc:'#FFB6C1' -gravity center -pointsize 10 -fill white -annotate +0+0 "$label" items/ingredients/${ingredient}_raw.png
        convert -size 48x48 xc:'#FF69B4' -gravity center -pointsize 10 -fill white -annotate +0+0 "$label" items/ingredients/${ingredient}_chopped.png
        convert -size 48x48 xc:'#FFA500' -gravity center -pointsize 10 -fill white -annotate +0+0 "$label" items/ingredients/${ingredient}_cooking.png
        convert -size 48x48 xc:'#8B4513' -gravity center -pointsize 10 -fill white -annotate +0+0 "$label" items/ingredients/${ingredient}_cooked.png
        convert -size 48x48 xc:'#000000' -gravity center -pointsize 10 -fill white -annotate +0+0 "$label" items/ingredients/${ingredient}_burned.png
    done
    
    echo "✅ All placeholder images generated successfully!"
    
else
    echo "⚠️  ImageMagick not found. Install it with: brew install imagemagick"
    echo "Or add your own 48x48 PNG images manually to the directories."
    echo ""
    echo "Required files:"
    echo "  stations/*.png (7 files)"
    echo "  items/kitchen_utensils/*.png (2 files)"
    echo "  items/ingredients/*_[raw|chopped|cooking|cooked|burned].png (25 files)"
fi
