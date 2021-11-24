from typing import Any


class Comparator:
    i1: int
    i2: int

    def __init__(self, i1: int, i2: int) -> None:
        self.i1 = i1
        self.i2 = i2

    def overlaps(self, other: 'Comparator') -> bool:
        return ((self.i1 < other.i1 < self.i2) or
                (self.i1 < other.i2 < self.i2) or
                (other.i1 < self.i1 < other.i2) or
                (other.i1 < self.i2 < other.i2))

    def has_same_input(self, other: 'Comparator') -> bool:
        return (self.i1 == other.i1 or
                self.i1 == other.i2 or
                self.i2 == other.i1 or
                self.i2 == other.i2)


def get_max_input(comparators: list[Comparator]) -> int:
    max_input = 0
    for c in comparators:
        if c.i2 > max_input:
            max_input = c.i2
    return max_input


def convert(indices: list[int], path: str) -> None:
    comparators: list[Comparator] = []
    for i in range(1, len(indices), 2):
        comparators.append(Comparator(indices[i - 1], indices[i]))

    scale = 1
    x_scale = scale * 35
    y_scale = scale * 20

    comparators_svg = ''
    w = x_scale
    group: dict[Comparator, float] = {}
    for c in comparators:
        for other in group:
            if c.has_same_input(other):
                for (_, pos) in group.items():
                    if pos > w:
                        w = pos
                w += x_scale
                group.clear()
                break

        cx = w
        for (other, other_pos) in group.items():
            if other_pos >= cx and c.overlaps(other):
                cx = other_pos + x_scale / 3

        y0 = y_scale + c.i1 * y_scale
        y1 = y_scale + c.i2 * y_scale
        comparators_svg += (
            "<circle cx='%s' cy='%s' r='%s' style='stroke:black;stroke-width:1;fill=yellow' />" % (cx, y0, 3) +
            "<line x1='%s' y1='%s' x2='%s' y2='%s' style='stroke:black;stroke-width:%s' />" % (cx, y0, cx, y1, 1) +
            "<circle cx='%s' cy='%s' r='%s' style='stroke:black;stroke-width:1;fill=yellow' />" % (cx, y1, 3)
        )
        group[c] = cx

    lines_svg = ''
    w += x_scale
    n = get_max_input(comparators) + 1
    for i in range(n):
        y = y_scale + i * y_scale
        lines_svg += (
            "<line x1='%s' y1='%s' x2='%s' y2='%s' style='stroke:black;stroke-width:%s' />" %
                (0, y, w, y, 1)
        )

    h = (n + 1) * y_scale
    with open(path, 'w') as fp:
        fp.write(
            "<?xml version='1.0' encoding='utf-8'?>" +
			"<!DOCTYPE svg>" +
			"<svg width='%spx' height='%spx' xmlns='http://www.w3.org/2000/svg'>" % (w, h) +
			comparators_svg +
			lines_svg +
			"</svg>"
        )
