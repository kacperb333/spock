package org.spockframework.smoke.condition

import org.spockframework.EmbeddedSpecification
import org.spockframework.runtime.SpockComparisonFailure
import spock.lang.Issue

class DiffedObjectRendering extends EmbeddedSpecification {

  @Issue("http://issues.spockframework.org/detail?id=170")
  def "can handle null values"() {
    when:
    runner.runFeatureBody("expect: 1 == null")

    then:
    SpockComparisonFailure failure = thrown()
    failure.actual == "1\n"
    failure.expected == "null\n"
  }

  @Issue("https://github.com/spockframework/spock/issues/659")
  def 'it should render a bean properly'() {
    when:
    runner.runFeatureBody '''
        setup:
        def b1 = new org.spockframework.smoke.condition.DiffedObjectRendering.MyBean([integer: 1, string: "fun"])
        def b2 = new org.spockframework.smoke.condition.DiffedObjectRendering.MyBean([integer: 2, string: "fun2"])

        expect:
        b1 == b2
    '''

    then:
    SpockComparisonFailure failure = thrown()
    failure.actual == "integer: 1\nstring: fun\n"
    failure.expected == "integer: 2\nstring: fun2\n"
  }

  @Issue("https://github.com/spockframework/spock/issues/659")
  def 'it should render a bean sub-class properly'() {
    when:
    runner.runFeatureBody '''
        setup:
        def b1 = new org.spockframework.smoke.condition.DiffedObjectRendering.MySubBean([integer: 1, string: "fun"])
        def b2 = new org.spockframework.smoke.condition.DiffedObjectRendering.MySubBean([integer: 2, string: "fun2"])

        expect:
        b1 == b2
    '''

    then:
    SpockComparisonFailure failure = thrown()
    failure.actual == "integer: 1\nstring: fun\n"
    failure.expected == "integer: 2\nstring: fun2\n"
  }

  @Issue("https://github.com/spockframework/spock/issues/659")
  def 'it should render a bean with an interface properly'() {
    when:
    runner.runFeatureBody '''
        setup:
        def b1 = new org.spockframework.smoke.condition.DiffedObjectRendering.MyBean2([long: 1, string: "fun"])
        def b2 = new org.spockframework.smoke.condition.DiffedObjectRendering.MyBean2([long: 2, string: "fun2"])

        expect:
        b1 == b2
    '''

    then:
    SpockComparisonFailure failure = thrown()
    failure.actual == "long1: 1\nstring: fun\n"
    failure.expected == "long1: 2\nstring: fun2\n"
  }

  @Issue("https://github.com/spockframework/spock/issues/659")
  def 'it should render a bean sub-class with an interface properly'() {
    when:
    runner.runFeatureBody '''
        setup:
        def b1 = new org.spockframework.smoke.condition.DiffedObjectRendering.MySubBean2([long: 1, string: "fun"])
        def b2 = new org.spockframework.smoke.condition.DiffedObjectRendering.MySubBean2([long: 2, string: "fun2"])

        expect:
        b1 == b2
    '''

    then:
    SpockComparisonFailure failure = thrown()
    failure.actual == "long1: 1\nstring: fun\n"
    failure.expected == "long1: 2\nstring: fun2\n"
  }

  @Issue("https://github.com/spockframework/spock/issues/909")
  def 'render class properly'() {
    when:
    runner.runFeatureBody '''
       expect:
        new SocketTimeoutException().getClass() == new ClassNotFoundException().getClass()
    '''

    then:
    SpockComparisonFailure failure = thrown()
    failure.actual == "class java.net.SocketTimeoutException\n"
    failure.expected == "class java.lang.ClassNotFoundException\n"
  }

  interface RenderBean {
    long getLong()
  }

  static class MyBean {
    private int integer
    private String string

    int getInteger() {
      return integer
    }

    void setInteger(int integer) {
      this.integer = integer
    }

    String getString() {
      return string
    }

    void setString(String string) {
      this.string = string
    }

    @Override
    boolean equals(Object o) {
      if (this.is(o))
        return true
      if (o == null || getClass() != o.getClass())
        return false
      MyBean myBean = (MyBean)o
      return integer == myBean.integer &&
        Objects.equals(string, myBean.string)
    }

    @Override
    int hashCode() {
      return Objects.hash(integer, string)
    }

  }

  static class MyBean2 implements RenderBean {
    private long long1
    private String string

    long getLong() {
      return long1
    }

    void setLong(long long1) {
      this.long1 = long1
    }

    String getString() {
      return string
    }

    void setString(String string) {
      this.string = string
    }

    @Override
    boolean equals(Object o) {
      if (this.is(o))
        return true
      if (o == null || getClass() != o.getClass())
        return false
      MyBean2 myBean = (MyBean2)o
      return long1 == myBean.long1 &&
        Objects.equals(string, myBean.string)
    }

    @Override
    int hashCode() {
      return Objects.hash(long1, string)
    }

  }


  static class MySubBean extends MyBean {
  }

  static class MySubBean2 extends MyBean2 {
  }

}

