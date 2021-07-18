from typing import Optional, Union

import pygame
import pygame.display
import pygame.draw
import pygame.event
import pygame.font
import pygame.time
from pygame import *
from pygame.locals import *


class Editor:
    nodes: list['Node']
    connections: list['Connection']
    selected: Optional['Node']
    dragging: Optional['Connection']
    drag_candidate: Optional['Node']

    def __init__(self, shuffles: list[str]) -> None:
        self.nodes = [Node('', self, -Node.WIDTH, 15)]
        self.nodes.extend(Node(shuffle, self) for shuffle in shuffles)
        self.connections = []
        self.selected = None
        self.dragging = None
        self.drag_candidate = None

    def draw(self, surface: Surface):
        for connection in self.connections:
            connection.draw(surface)
        for node in self.nodes:
            node.draw(surface)

    def drag(self, rel: Vector2):
        if self.selected is not None:
            self.selected.drag(rel)
        elif self.dragging is not None:
            pos = self.dragging.current_drag_pos
            pos += rel
            for node in reversed(self.nodes):
                if node.in_area(pos):
                    self.drag_candidate = node
                    break
            else:
                self.drag_candidate = None

    def select(self, pos: Vector2):
        for node in reversed(self.nodes):
            if node.in_area(pos):
                self.selected = node
                return
            elif node.in_start_drag(pos):
                new_connection = Connection(node, pos)
                removed = 0
                for (i, conn) in enumerate(self.connections.copy()):
                    if conn.from_ == node:
                        del self.connections[i - removed]
                        removed += 1
                self.connections.append(new_connection)
                self.dragging = new_connection
                node.post_connection = new_connection
                self.selected = None
                return
        self.selected = None

    def end_connection(self):
        if self.dragging is None:
            return
        if self.drag_candidate is not None:
            self.dragging.finish_dragging(self.drag_candidate)
        else:
            self.connections.remove(self.dragging)
        self.dragging = None
        self.drag_candidate = None

    def delete(self):
        if self.selected is not None:
            self.selected.delete()


class Node:
    WIDTH  = 250
    HEIGHT = 50

    shuffle: str
    x: int
    y: int
    editor: Editor
    pre_connection: Optional['Connection']
    post_connection: Optional['Connection']

    def __init__(self, shuffle: str, editor: Editor, x: int = 0, y: int = 0) -> None:
        self.shuffle = shuffle
        self.editor = editor
        self.x = x
        self.y = y
        self.pre_connection = None
        self.post_connection = None

    def draw(self, surface: Surface):
        border_color = (128, 128, 255) if self is self.editor.selected else (0, 0, 0)
        left_color = (128, 128, 255) if self is self.editor.drag_candidate else border_color
        pygame.draw.circle(surface, left_color, (self.x, self.y + Node.HEIGHT / 2), 10)
        pygame.draw.circle(surface, border_color, (self.x + Node.WIDTH, self.y + Node.HEIGHT / 2), 10)
        pygame.draw.rect(surface, (255, 255, 255), (self.x, self.y, Node.WIDTH, Node.HEIGHT))
        pygame.draw.rect(surface, border_color, (self.x, self.y, Node.WIDTH, Node.HEIGHT), 2)
        text_r = font.render(self.shuffle, True, (0, 0, 0))
        rect = text_r.get_rect()
        surface.blit(
            text_r,
            rect.move(self.x, self.y)
                .move(Node.WIDTH / 2, Node.HEIGHT / 2)
                .move(-rect.width / 2, -rect.height / 2)
        )

    def drag(self, rel: Vector2):
        self.x += rel.x
        self.y += rel.y

    def in_area(self, pos: Vector2):
        return Rect(self.x, self.y, Node.WIDTH, Node.HEIGHT).collidepoint(pos)

    def in_start_drag(self, pos: Vector2):
        return Rect(self.x + Node.WIDTH, self.y + Node.HEIGHT / 2 - 10, 10, 20).collidepoint(pos)

    def delete(self):
        if self == self.editor.selected:
            self.editor.selected = None
        self.editor.nodes.remove(self)
        if self.pre_connection is not None:
            self.pre_connection.to = self.post_connection.to
        if self.post_connection is not None:
            self.post_connection.from_ = self.pre_connection.from_

    def __eq__(self, o: object) -> bool:
        if isinstance(o, Node):
            return (
                self.editor is o.editor
                and self.shuffle == o.shuffle
                and self.x == o.x
                and self.y == o.y
                and self.pre_connection is o.pre_connection
                and self.post_connection is o.post_connection
            )
        return False

    def get_pos(self) -> Vector2:
        return Vector2(self.x, self.y)


class Connection:
    from_: Node
    to: Optional[Node]
    current_drag_pos: Vector2

    def __init__(self, from_: Node, to: Union[Node, Vector2]) -> None:
        self.from_ = from_
        if isinstance(to, Node):
            self.to = to
            self.current_drag_pos = Vector2()
        else:
            self.to = None
            self.current_drag_pos = to

    def draw(self, surface: Surface):
        from_pos = self.from_.get_pos() + (Node.WIDTH + 10, Node.HEIGHT / 2)
        end_pos: Vector2 = Vector2(self.current_drag_pos) if self.to is None else (self.to.get_pos() + (0, Node.HEIGHT / 2))
        end_pos -= (10, 0)
        mid_start = from_pos + (15, 0)
        mid_end = end_pos - (15, 0)
        pygame.draw.line(surface, (0, 0, 0), from_pos, mid_start, 5)
        pygame.draw.line(surface, (0, 0, 0), mid_start, mid_end, 5)
        pygame.draw.line(surface, (0, 0, 0), mid_end, end_pos, 5)
        if self.to is None:
            pygame.draw.circle(surface, (0, 0, 0), end_pos + (10, 0), 10)

    def finish_dragging(self, other: Node):
        self.to = other
        other.pre_connection = self
        removed = 0
        for (i, conn) in enumerate(other.editor.connections.copy()):
            if conn == self:
                continue
            if conn.to == other:
                del other.editor.connections[i - removed]
                removed += 1


pygame.init()
screen = pygame.display.set_mode((1280, 720), RESIZABLE)
font = pygame.font.SysFont('ariel', 24)

editor = Editor(['Linear', 'Randomly', 'Backwards', 'Slight Shuffle', 'No Shuffle'])


clock = pygame.time.Clock()
running = True
while running:
    delta = clock.tick(60) / 1000
    for event in pygame.event.get():
        if event.type == QUIT:
            running = False
        elif event.type == MOUSEMOTION:
            if event.buttons[0]:
                editor.drag(Vector2(event.rel))
        elif event.type == MOUSEBUTTONDOWN:
            if event.button == 1:
                editor.select(Vector2(event.pos))
        elif event.type == MOUSEBUTTONUP:
            if event.button == 1:
                editor.end_connection()
        elif event.type == KEYDOWN:
            if event.key == K_DELETE:
                editor.delete()

    screen.fill((128, 128, 128))

    editor.draw(screen)

    pygame.display.update()
