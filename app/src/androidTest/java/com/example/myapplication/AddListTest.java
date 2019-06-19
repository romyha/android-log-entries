package com.example.myapplication;

import android.support.test.rule.ActivityTestRule;

import androidx.test.espresso.Espresso;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static org.junit.Assert.*;

public class AddListTest {

    @Rule
    public ActivityTestRule<AddList> mActivityTestRule = new ActivityTestRule<AddList>(AddList.class);

    private String nName = "List";

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testUserListNameInputScenario() {
        //perform button click
        Espresso.onView(withId(R.id.button3)).perform(click());
        //input text in the edit text
        Espresso.onView(withId(R.id.listName)).perform(typeText(nName));
        //perform button click
        Espresso.onView(withId(R.id.button)).perform(click());
        //close soft keyboard
        Espresso.closeSoftKeyboard();
        //go back
        Espresso.pressBack();
        //checking text in text view
        Espresso.onView(withId(R.id.shoppingLists)).check(matches(withText(nName)));
    }

    @After
    public void tearDown() throws Exception {
    }

}