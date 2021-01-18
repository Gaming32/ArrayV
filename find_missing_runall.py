import os
import glob
import re


threadfile_glob = 'src/threads/Run*Sorts.java'
def_patt = re.compile(r'^\s*private Sort ([a-zA-Z]+);$', re.MULTILINE)


included_sorts = []

for threadfile in glob.iglob(threadfile_glob):
    if os.path.basename(threadfile) in ('RunAllSorts.java', 'RunScriptedSorts.java'):
        continue
    with open(threadfile) as fp:
        contents = fp.read()
        for match in def_patt.finditer(contents):
            included_sorts.append(match.group(1))


sortfile_glob = 'src/sorts/*/*.java'
package_patt = re.compile(r'^package sorts\.([a-zA-Z]+);$', re.MULTILINE)
class_patt = re.compile(r'^\s+public\s*([a-zA-Z0-9]+)\(ArrayVisualizer arrayVisualizer\)\s*\{\s*$', re.MULTILINE)


for sortfile in glob.iglob(sortfile_glob):
    with open(sortfile) as fp:
        contents = fp.read()
    package = package_patt.search(contents).group(1)
    if package == 'templates':
        continue
    class_name = class_patt.search(contents).group(1)
    if class_name not in included_sorts:
        print(package, class_name, sep='.')
