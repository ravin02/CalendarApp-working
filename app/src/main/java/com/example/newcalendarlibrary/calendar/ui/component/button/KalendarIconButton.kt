
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector


/**

An internal composable function that renders an icon button for the Kalendar library.*
@param imageVector The vector icon to display.
@param modifier The modifier for the icon button.
@param contentDescription The content description for accessibility.
@param onClick The callback function to invoke when the button is clicked.
 */
@Composable
internal fun KalendarIconButton(
    imageVector: ImageVector,  // The vector icon to display
    modifier: Modifier = Modifier,  // Modifier for the icon button
    contentDescription: String? = null,  // Content description for accessibility
    onClick: () -> Unit = {}  // Callback function to invoke when the button is clicked
) {
    IconButton(
        onClick = onClick,  // Assign the onClick callback to the IconButton
        modifier = modifier
            .wrapContentSize()  // Wrap content size with the specified modifier
            .clip(CircleShape)  // Clip the icon button into a circle shape
    ) {
        Icon(
            modifier = Modifier,  // Modifier for the Icon
            tint = Color(0xFF413D4B),  // Tint color for the icon
            imageVector = imageVector,  // Vector icon to display
            contentDescription = contentDescription  // Content description for the icon
        )
    }
}