package com.jgeodesy;

import org.junit.After;
import org.junit.Before;

/**
 * Created by omeruluoglu on 30.10.2019.
 */
public interface BaseUnitTest {

    @Before
    void setUp();

    @After
    void clean();
}
