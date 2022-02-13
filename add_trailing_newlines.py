import os

# linesep = os.linesep.encode('ascii')
linesep = b'\n'


def dir_parts(dir: str) -> tuple[str]:
    return tuple(os.path.normpath(dir).split(os.sep))


def is_in_dir(dir: str, in_: tuple[str]) -> bool:
    return tuple(dir.split(os.sep, len(in_)))[:len(in_)] == in_


def is_in_dirs(dir: str, in_: tuple[tuple[str]]) -> bool:
    return any(is_in_dir(dir, i) for i in in_)


def add_trailing_newlines(path: str) -> bool:
    with open(path, 'r+b') as fp:
        fp.seek(-len(linesep), 2)
        if fp.read(len(linesep)) != linesep:
            fp.write(linesep)
            return True
    return False


skip_parts = (
    '.git',
    'bin',
    'dist',
    'cache',
    '__pycache__',
    'lib',
    'sorting_networks',
    'src/main/resources',
    'wrapper'
)
skip_parts = tuple(dir_parts(d) for d in skip_parts)

for (dir, dirs, files) in os.walk('.'):
    if dir == '.':
        dir = ''
    else:
        dir = os.path.normpath(dir)
    if is_in_dirs(dir, skip_parts):
        continue
    for file in files:
        file = os.path.join(dir, file)
        if add_trailing_newlines(file):
            print('Wrote  ', file)
        else:
            print('Skipped', file)
