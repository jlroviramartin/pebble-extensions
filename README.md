# pebble-extensions
Extensions made for Pebble project.

This project contains some extensions made for the project [Pebble](http://www.mitchellbosecke.com/pebble/home).

## *Filters*

_NlFilter (nl)_: this filter converts the "%n" char sequences into system newlines (\n, \r\n, ...).

```twig
{{ "Line 1%nLine 2%nLine 3%n" | nl }}
```

Output
```
Line 1
Line 2
Line 3
```

_IndentFilter (indent)_: this filter indents the text using space characters.
It indents AFTER a new line character, so the first char sequence up to the first new line is NOT indented.
The reason is you can reuse the previous indentation. Also, it doesn't indent empty lines.
It multiplies the number of indentations by 4 by default. 

```twig
{# We reuse the four-space indentation and indent the text by 1 (* 4 spaces). #}
    {{ "NOT indented%nIndented%nIndented" | nl | indent(1) }}
```

Output
```
    NOT indented
    Indented
    Indented
```

_ReverseFilter (reverse)_: this filter reverses the list, array...

---

## *Nodes*

_DynamicMacroNode (dynmacro)_: this node is similar to the macro node, but it allows you to use as a delegate/lambda function.

```twig
{# Macro 'Lambda' is defined to call it as a lambda macro. #}
{% dynmacro Lambda( a, b, c ) %}
{{ a }}, {{ b }}, {{ c }}
{%- enddynmacro %}

{{ MyMethod( Lambda ) }}

{% macro MyMethod( macroToCall ) %}
{# 'dynamic' is used to dynamically call macro 'macroToCall'. #}
{# -> macroToCall( 1, "String", 1.5 ) #}
Result: {{ dynamic( macroToCall, [ 1, "String", 1.5 ] ) }}
{%- endmacro %}
```

Output
```
Result: 1, String, 1.5
```

---

## *Functions*

_ReflectionFunction (reflection)_: this function is used to call a method by reflection.

_DynamicFunction (dynamic)_: this function is used to call a lambda macro (see dynmacro example).

_ParentFunction (p)_: this function is used inside a dynamic macro definition and it allows you to reuse the parameters of the container macro.

```twig
{% macro Macro1( a ) %}
    {%- dynmacro Macro2( b ) %}
{# We are using the Macro1 parameters. #}
Result: {{ p().a }}, {{ b }}
    {%- enddynmacro %}
{{ Macro2("Texto interno") }}
{% endmacro %}

{{ Macro1("Texto externo") }}
```

Output
```
Result: Outer text, Inner text
```

_InvokeMacroFunction (invoke)_: this function is used to call a macro using its name.

```twig
{% macro Test( a, b, c ) %}
Result: {{ a }}, {{ b }}, {{ c }}
{%- endmacro %}
{% set mname = \"Test\" %}
{{ invoke( mname, [ \"Arg 1\", \"Arg 2\", \"Arg 3\" ] ) }}
```

Output
```
Result: Arg 1, Arg 2, Arg 3
```
