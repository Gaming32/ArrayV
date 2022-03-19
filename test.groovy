import groovy.transform.CompileStatic

@CompileStatic
class test extends ArrayVScript {
    @CompileStatic
    def sortFn(int[] arr, int length, int c, int d, int e) {
        for (def i = length; i > 1; i--) {
            for (def j = 1; j < i; j++) {
                if (arr[j - 1] > arr[j]) {
                    def temp = arr[j - 1];
                    arr[j - 1] = arr[j];
                    arr[j] = temp;
                }
            }
        }
    }

    def run() {
        def sort = newSort(this.&sortFn) {
            listName "Groovy"
            category "Tests"
        }
        println sort
        registerEventHandler(EventType.ARRAYV_FULLY_LOADED) {
            println "Fully loaded!"
        }
    }
}
