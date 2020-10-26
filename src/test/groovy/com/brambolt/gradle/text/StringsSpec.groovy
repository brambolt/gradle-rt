package com.brambolt.gradle.text

import static Strings.firstToUpperCase

class StringsSpec {

  def 'can uppercase first letter'() {
    given:
    def camelCase = 'camelCase'
    def pascalCase = 'PascalCase'
    when:
    def fCamelCase = firstToUpperCase(camelCase)
    def fPascalCase = firstToUpperCase(pascalCase)
    then:
    'CamelCase' == fCamelCase
    pascalCase == fPascalCase
  }
}
