package com.jgeodesy;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by omeruluoglu on 30.10.2019.
 */
@RunWith(JUnit4.class)
public abstract class AbstractUnitTest {

    final protected Logger logger = LoggerFactory.getLogger(getClass().getName());
}