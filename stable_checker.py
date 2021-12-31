from __future__ import annotations
from typing import Any, Callable, Generator, Iterable, List, Literal, Sequence, Union
import random


class IntegerWrapper(int):
    key: Any = None
    # def __init__(self, val, key) -> None:
    #     super().__init__(val)
    #     self.key = key

    @classmethod
    def wrap_int(cls, val, key) -> IntegerWrapper:
        res = cls(val)
        res.key = key
        return res

    def key_compare(self, other: IntegerWrapper) -> Literal[None, -1, 0, 1]:
        if self < other:
            return -1
        elif self > other:
            return 1
        elif self.key < other.key:
            return -1
        elif self.key > other.key:
            return 1
        return 0

    def __repr__(self) -> str:
        return f'{super().__repr__()}:{self.key!r}'

    @staticmethod
    def is_sequence_stable(seq: Sequence[IntegerWrapper]):
        return all(seq[i].key_compare(seq[i + 1]) == -1 for i in range(len(seq) - 1))

    def __wrapper(funcname: str) -> Callable[..., IntegerWrapper]:
        funcname = f'__{funcname}__'
        def caller(self, *args):
            res = getattr(int, funcname)(self, *args)
            return IntegerWrapper.wrap_int(res, self.key)
        return caller

    __abs__ = __wrapper('abs')
    __add__ = __wrapper('add')
    __and__ = __wrapper('and')
    __ceil__ = __wrapper('ceil')
    __divmod__ = __wrapper('divmod')
    __floor__ = __wrapper('floor')
    __floordiv__ = __wrapper('floordiv')
    __invert__ = __wrapper('invert')
    __lshift__ = __wrapper('lshift')
    __mod__ = __wrapper('mod')
    __mul__ = __wrapper('mul')
    __neg__ = __wrapper('neg')
    __or__ = __wrapper('or')
    __pos__ = __wrapper('pos')
    __pow__ = __wrapper('pow')
    __radd__ = __wrapper('radd')
    __rand__ = __wrapper('rand')
    __rdivmod__ = __wrapper('rdivmod')
    __rfloordiv__ = __wrapper('rfloordiv')
    __rlshift__ = __wrapper('rlshift')
    __rmod__ = __wrapper('rmod')
    __rmul__ = __wrapper('rmul')
    __ror__ = __wrapper('ror')
    __round__ = __wrapper('round')
    __rpow__ = __wrapper('rpow')
    __rrshift__ = __wrapper('rrshift')
    __rshift__ = __wrapper('rshift')
    __rsub__ = __wrapper('rsub')
    __rtruediv__ = __wrapper('rtruediv')
    __rxor__ = __wrapper('rxor')
    __sub__ = __wrapper('sub')
    __truediv__ = __wrapper('truediv')
    __trunc__ = __wrapper('trunc')
    __xor__ = __wrapper('xor')


def generate_list(list_size=4096, list_start=0, num_equal=4) -> Generator[int]:
    current = list_start
    for i in range(list_size):
        yield int(current)
        current += 1 / num_equal


UsableCallable = Callable[[List[IntegerWrapper]], Any]


def generate_keyed_list(vals: Iterable[int]) -> Generator[IntegerWrapper]:
    cur_key = {}
    for prim in vals:
        cur_key.setdefault(prim, cur_key.get(prim, -1) + 1)
        yield IntegerWrapper.wrap_int(prim, cur_key[prim])


def simple_sort(func: UsableCallable, shuffler: UsableCallable = random.shuffle, list_size=4096, list_start=1, num_equal=4) -> List[IntegerWrapper]:
    # Create the list
    use_list = list(generate_keyed_list(generate_list(list_size, list_start, num_equal)))

    shuffler(use_list) # Shuffle the list
    func(use_list) # Sort the list

    return use_list


def test_function_stable(func: UsableCallable, shuffler: UsableCallable = random.shuffle, list_size=4096, list_start=1, num_equal=4) -> bool:
    return IntegerWrapper.is_sequence_stable(simple_sort(func, shuffler, list_size, list_start, num_equal))


def inplace_sort(arr: list):
    arr.sort()


def main(colonfunc: str):
    funcpath = colonfunc.split(':', 1)
    if len(funcpath) < 2:
        print('Syntax: ./stable_checker.py <module>:<func>')
        print('   Colon expected')
        return 1
    module, funcname = funcpath
    try:
        module = __import__(module)
    except ImportError as exc:
        print('Error importing module:', exc)
        return 1
    if not hasattr(module, funcname):
        print('Module', repr(module.__name__), 'has no attribute', repr(funcname))
        sys.exit(1)
    func = getattr(module, funcname)

    result = simple_sort(func)
    stable = IntegerWrapper.is_sequence_stable(result)
    print(colonfunc, ' is', ' not' * (not stable), ' stable', sep='')
    print('   ', result[:10], '...')


if __name__ == '__main__':
    import sys
    if len(sys.argv) < 2:
        print('Syntax: ./stable_checker.py <module>:<func>')
        sys.exit(1)
    sys.exit(main(sys.argv[1]))
