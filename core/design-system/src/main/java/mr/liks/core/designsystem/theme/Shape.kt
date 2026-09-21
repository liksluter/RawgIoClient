package mr.liks.core.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/** Формы */
val RawgShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

/** Скругление карточки игры в ленте */
val CardShape = RoundedCornerShape(16.dp)

/** Скругление превью */
val MediaShape = RoundedCornerShape(12.dp)

/** Скругление бейджа с рейтингом */
val RatingShape = RoundedCornerShape(6.dp)