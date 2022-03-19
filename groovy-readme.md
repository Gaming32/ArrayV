<!-- grip --user-content --context=Gaming32/ArrayV --browser groovy-readme.md -->

@EmeraldBlock asked me to create a more in-depth overview of this PR, so here we are.

I don't know what to write after a few minutes of thinking, so I'll just post the javadocs on the various methods.

## Javadocs for GroovyLocals

### `public static ArrayVisualizer getArrayv()`

Property for the main ArrayVisualizer instance.

Intended to be used like this (example):

```groovy
println arrayv.sortAnalyzer
```

#### Returns:
The main ArrayVisualizer instance

### `public static SortInfo getSort(String internalName)`

Get a sort by its internal name, which is usually the name of the sort class.

#### Parameters:
`internalName` - The internal name to find the sort by

#### Returns:
The sort with this internal name or `null` if no sort with the given internal name was found

### `public static SortInfo getSort(String name, SortNameType nameType)`

Get a sort by name

#### Parameters:
`name` - The name of the sort
`nameType` - The type of name to search by (such as list name, run name, etc.). See SortNameType for more details

#### Returns:
The sort with this name or `null` if no sort with the given name and name type was found

### `public static SortInfo newSort(Closure<?> sort, @DelegatesTo(Builder.class) Closure<?> metadata)`

Creates (and adds) a new sorting algorithm to this ArrayV instance.

Here's an example:

```groovy
def sortFn(array, length) {
    // Sorting algorithm code here
}

newSort(this::sortFn) {
    listName "My Custom Sort"
    category "Examples"
    unreasonableLimit 1024
}
```

#### Parameters:
`sort` - The sort method/function/closure to add. This closure can follow any of the signatures listed in SortFunctionSignatures
`metadata` - The closure used to define metadata. See above for an example, and SortInfo.Builder for the list of metadata methods you can use.

#### Returns:
The SortInfo object associated with the newly created algorithm
