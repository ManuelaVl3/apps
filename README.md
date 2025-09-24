# Carpeta de Imágenes - Drawable Resources

Esta carpeta contiene todos los recursos de imágenes (drawables) del proyecto Android.

## Estructura de carpetas:

- **`icons/`** - Iconos de la aplicación (botones, menús, etc.)
- **`backgrounds/`** - Imágenes de fondo y patrones
- **`illustrations/`** - Ilustraciones y gráficos de la aplicación

## Formatos soportados:
- PNG (recomendado para iconos con transparencia)
- JPG/JPEG (para fotografías)
- WebP (formato optimizado de Google)
- SVG (convertido a XML drawable)
- XML (drawables vectoriales)

## Convenciones de nombres:
- Usa nombres en minúsculas
- Separa palabras con guiones bajos: `ic_menu_settings.png`
- Prefijos recomendados:
  - `ic_` para iconos
  - `bg_` para fondos
  - `img_` para imágenes generales

## Densidades soportadas:
Para imágenes bitmap, considera crear versiones para diferentes densidades:
- `drawable-mdpi/` (160 dpi)
- `drawable-hdpi/` (240 dpi)
- `drawable-xhdpi/` (320 dpi)
- `drawable-xxhdpi/` (480 dpi)
- `drawable-xxxhdpi/` (640 dpi)
