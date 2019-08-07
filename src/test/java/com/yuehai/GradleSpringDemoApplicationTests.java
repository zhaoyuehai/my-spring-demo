package com.yuehai;

import org.springframework.beans.factory.annotation.Autowired;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GradleSpringDemoApplicationTests {

    @Autowired
    private Person person;
    @Autowired
    private Car car;

    @Test
    public void test() {
        person.toString();
    }
}