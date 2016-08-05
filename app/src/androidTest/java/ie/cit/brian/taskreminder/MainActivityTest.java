package ie.cit.brian.taskreminder;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ie.cit.brian.taskreminder.activities.LocationActivity;
import ie.cit.brian.taskreminder.activities.LoginActivity;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    private static final String STRING_TYPED = "Brian";

    @Rule
    public ActivityTestRule<LoginActivity> mLoginActivityRule =
            new ActivityTestRule<>(LoginActivity.class);

    @Rule
    public ActivityTestRule<LocationActivity> mLocationOffActRule =
            new ActivityTestRule(LocationActivity.class);

    @Test
    public void greeterSaysHello(){
        onView(withId(R.id.edT_login_name))
                .perform(typeText(STRING_TYPED));
        onView(withId(R.id.login_btn_name))
                .perform(click());
        onView(withText(STRING_TYPED))
                .check(matches(isDisplayed()));

    }




}
