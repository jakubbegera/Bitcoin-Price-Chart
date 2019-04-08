package cz.begera.bitcoin_price_chart


import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import cz.begera.bitcoin_price_chart.presentation.home.HomeActivity
import org.hamcrest.core.IsNot
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class HomeActivityInstrumentedTest {

    // TODO replace Thread.sleep() by Idling resource

    @get:Rule
    val activityRule = ActivityTestRule(
        HomeActivity::class.java, false, false
    )

    @Before
    fun setup() {
        activityRule.launchActivity(null)
    }

    @Test
    @Throws(Exception::class)
    fun launchAndCheckLoading() {
        Espresso.onView(ViewMatchers.withId(R.id.chip_timespan_2years))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.prb_loading))
            .check(matches(isDisplayed()))
        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.prb_loading))
            .check(matches(IsNot.not(isDisplayed())))
    }

    @Test
    @Throws(Exception::class)
    fun performSmokeTest() {
        val open = { id: Int ->
            Espresso.onView(ViewMatchers.withId(id))
                .perform(ViewActions.click())
            Thread.sleep(1500)
            Espresso.onView(ViewMatchers.withId(R.id.chart))
                .check(matches(isDisplayed()))
        }

        open(R.id.chip_timespan_2years)
        open(R.id.chip_timespan_1year)
        open(R.id.chip_timespan_30days)
        open(R.id.chip_timespan_60days)
        open(R.id.chip_timespan_180days)

    }

}
