package com.example.dicodingstory.ui.main

import android.view.View
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.dicodingstory.R
import com.example.dicodingstory.ui.login.LoginActivity
import com.example.dicodingstory.utils.EspressoIdlingResource
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    @get:Rule
    val activity = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun login_Success() {
        Intents.init()
        onView(withId(R.id.button_login)).check(matches(isDisplayed()))
        onView(withId(R.id.button_register)).check(matches(isDisplayed()))
        onView(withId(R.id.button_login)).perform(click())

        intended(hasComponent(LoginActivity::class.java.name))
        onView(withId(R.id.ed_login_email)).check(matches(isDisplayed()))
        onView(withId(R.id.ed_login_email)).perform(typeText("zaky@mail.com"))
        closeSoftKeyboard()
        onView(withId(R.id.ed_login_password)).check(matches(isDisplayed()))
        onView(withId(R.id.ed_login_password)).perform(typeText("zakystory"))
        closeSoftKeyboard()
        onView(withId(R.id.button_login)).check(matches(isDisplayed()))
        onView(withId(R.id.button_login)).perform(click())

        onView(isRoot()).perform(waitFor(5000))
        onView(withId(R.id.action_logout)).check(matches(isDisplayed()))
        onView(withId(R.id.action_logout)).perform(click())

        onView(withId(R.id.tv_welcome_title)).check(matches(isDisplayed()))
    }
}

fun waitFor(delay: Long): ViewAction? {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return isRoot()
        }

        override fun getDescription(): String? {
            return "wait for $delay milliseconds"
        }

        override fun perform(uiController: UiController, view: View?) {
            uiController.loopMainThreadForAtLeast(delay)
        }
    }
}