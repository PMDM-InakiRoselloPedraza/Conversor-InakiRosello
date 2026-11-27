# U2P4Conversor

**Autor:** Iñaki Rosello Pedraza · 2DAM

## 🎥 Vídeo explicativo

[ENLACE AL VÍDEO] https://youtu.be/ck1P8LyzfEQ?si=1v8s1_Ou7ApRtmSx

SE QUE EL VIDEO DURA MUCHO PERO QUERIA EXPLICAR TODO BIEN PARA NO DEJARME NADA Y QUE SE VIERA QUE SE QUE HAGO

## Descripción

La aplicación convierte valores entre distintas unidades de medida, en ambos sentidos:

- Pulgadas ↔ Centímetros
- Kilogramos ↔ Libras
- Kilómetros ↔ Millas

El resultado se muestra siempre con **dos decimales**.

## Funcionalidades

- Icono personalizado y imagen de fondo.
- Selección de la conversión mediante un `Spinner`.
- Validación de la entrada: solo se aceptan números mayores o iguales que 1, y el campo no puede estar vacío. El error se muestra en un `TextView` rojo, visible solo cuando se produce.
- Mensajes de `Log` del ciclo de vida de la Activity y de cada botón pulsado (etiqueta `LogsConversor`).
- El resultado y el error se conservan al rotar el dispositivo (`onSaveInstanceState` / `onRestoreInstanceState`).
- Layout específico para orientación horizontal (`layout-land`).
- Botón de ayuda (**?**) que abre una `HelpActivity` con instrucciones de uso y explicación de los errores.
- **Extra:** valoración de la app con un `RatingBar`, guardada de forma persistente en `SharedPreferences`. Una vez guardada, solo se muestra y no se puede modificar.

## Ramas

| Rama | Contenido |
|------|-----------|
| `guided` | Parte guiada: conversión de pulgadas a centímetros |
| `Activ1` | Conversión en ambos sentidos y resultado con dos decimales |
| `Activ2` | Excepciones y `TextView` de error |
| `Activ3` | Logs del ciclo de vida y de los botones |
| `Activ4` | Conservación del estado al rotar |
| `Activ5` | Nuevas conversiones, layout horizontal y Activity de ayuda |
| `Activ6` | Valoración persistente con `SharedPreferences` (extra) |
| `main` | Aplicación completa con todo el trabajo fusionado |



