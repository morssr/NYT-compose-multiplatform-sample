import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.mls.kmp.mor.nytnewskmp.MyApp
import com.mls.kmp.mor.nytnewskmp.ui.settings.SettingsViewModel
import com.mls.kmp.mor.nytnewskmp.ui.settings.ThemeConfig
import org.koin.compose.koinInject

@Composable
fun IosApp() {
    val settingsViewModel: SettingsViewModel = koinInject()
    val settingsUiStateState by settingsViewModel.state.collectAsState()

    val darkTheme = when (settingsUiStateState.theme) {
        ThemeConfig.FOLLOW_SYSTEM -> isSystemInDarkTheme()
        ThemeConfig.DARK -> true
        ThemeConfig.LIGHT -> false
    }

    MyApp(darkTheme)
}