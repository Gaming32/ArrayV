import os


def count_spaces_tabs(path):
    try:
        with open(path, encoding='utf-8') as fp:
            content = fp.read()
    except UnicodeDecodeError:
        print(f'Unable to decode {path}. Skipping...')
        return None, None
    spaces = content.count('    ')
    tabs = content.count('\t')
    return spaces, tabs


def print_spaces_tabs(path, spaces, tabs, force=False):
    if not force and (spaces == 0 or tabs == 0):
        return
    total = spaces + tabs
    percent_spaces = round(spaces / total * 100, 1)
    percent_tabs = round(tabs / total * 100, 1)
    if tabs > spaces:
        print(f'{path} has {percent_tabs:.1f}% tabs/{percent_spaces:.1f}% spaces ({tabs}/{spaces})')
    else:
        print(f'{path} has {percent_spaces:.1f}% spaces/{percent_tabs:.1f}% tabs ({spaces}/{tabs})')


def main():
    for (cur, dirs, files) in os.walk('src'):
        for file in files:
            file = os.path.join(cur, file)
            spaces, tabs = count_spaces_tabs(file)
            if spaces is None:
                continue
            print_spaces_tabs(file, spaces, tabs)


if __name__ == '__main__':
    main()
