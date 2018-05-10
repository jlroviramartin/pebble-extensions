# pebble-extensions
Extensions made for Pebble project

This project contains some extensions made for the project [Pebble](http://www.mitchellbosecke.com/pebble/home).

## *Filters*

_NlFilter (nl)_: this filter converts the "%n" char sequences into system newlines (\n, \r\n, ...).
Example:
```twig
{{ "Line 1%nLine 2%nLine 3%n" | nl }}
```
_IndentFilter (indent)_: this filter indents the text using space characters.
It indents AFTER a new line character, so the first char sequence up to the first new line is NOT indented.
```twig
{{ "Text to indent" | indent(2) }}
```
_ReverseFilter (reverse)_: this filter reverses the list, array...

---

## *Nodes*

_DynamicMacroNode (dynmacro)_: this node is similar to the macro node, but it allows you to use as a delegate/lambda function.
Example:
```twig
{# Macro 'Lambda' is defined to call it as a lambda macro. #}
{% dynmacro Lambda( a, b, c ) %}
    {{ a }} {{ b }} {{ c }}
{% enddynmacro %}

{{ MyMethod( Lambda ) }}

{% macro MyMethod( macroToCall ) %}
    {# 'dynamic' is used to dynamically call macro 'macroToCall'. #}
    {{ dynamic( macroToCall, [ 1, "String", 1.5 ] ) }} {# -> macroToCall( 1, "String", 1.5 ) #}
{% macro %}
```

---

## *Functions*

_ReflectionFunction (reflection)_: this function is used to call a method by reflection.
_DynamicFunction (dynamic)_: this function is used to call a lambda macro (see dynmacro example).
_ParentFunction (p)_: this function is used inside a macro/dynamic macro definition and it allows you to reuse the parameters of the container macro.
```twig
{% macro Macro1( a ) %}
    {% macro Macro2( b ) %}
        {{ p().a }} {{ b }} {# We are using the Macro1 parameters. #}
    {% endmacro %}
{% endmacro %}
```
