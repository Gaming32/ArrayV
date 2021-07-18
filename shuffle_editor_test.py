from typing import Optional
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

    def __init__(self, shuffles: list[str]) -> None:
        self.nodes = [Node('', self, -Node.WIDTH, 15)]
        self.nodes.extend(Node(shuffle, self) for shuffle in shuffles)
        self.connections = []
        self.selected = None

    def draw(self, surface: Surface):
        for connection in self.connections:
            connection.draw(surface)
        for node in self.nodes:
            node.draw(surface)

    def drag(self, rel: Vector2):
        if self.selected is not None:
            self.selected.drag(rel)

    def select(self, pos: Vector2):
        for node in reversed(self.nodes):
            if node.in_area(pos):
                self.selected = node
                return
        self.selected = None

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
        border = (128, 128, 255) if self.editor.selected is self else (0, 0, 0)
        pygame.draw.circle(surface, border, (self.x, self.y + Node.HEIGHT / 2), 10)
        pygame.draw.circle(surface, border, (self.x + Node.WIDTH, self.y + Node.HEIGHT / 2), 10)
        pygame.draw.rect(surface, (255, 255, 255), (self.x, self.y, Node.WIDTH, Node.HEIGHT))
        pygame.draw.rect(surface, border, (self.x, self.y, Node.WIDTH, Node.HEIGHT), 2)
        text_r = font.render(self.shuffle, True, (0, 0, 0))
        rect = text_r.get_rect()
        surface.blit(
            text_r,
            rect.move(self.x, self.y)
                .move(Node.WIDTH / 2, Node.HEIGHT / 2)
                .move(-rect.width / 2, -rect.height / 2)
        )
        self._hovered = False

    def drag(self, rel: Vector2):
        self.x += rel.x
        self.y += rel.y

    def in_area(self, pos: Vector2):
        return Rect(self.x, self.y, Node.WIDTH, Node.HEIGHT).collidepoint(pos)

    def delete(self):
        if self == self.editor.selected:
            self.editor.selected = None
        self.editor.nodes.remove(self)
        if self.pre_connection is not None and self.post_connection is not None:
            self.pre_connection.to = self.post_connection.to
        if self.post_connection is not None and self.pre_connection is not None:
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


class Connection:
    from_: Optional[Node]
    to: Optional[Node]

    def __init__(self, from_: Node = None, to: Node = None) -> None:
        self.from_ = from_
        self.to = to

    def draw(self, surface: Surface):
        pass


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
        elif event.type == KEYDOWN:
            if event.key == K_DELETE:
                editor.delete()

    screen.fill((128, 128, 128))

    dims = screen.get_size()

    editor.draw(screen)

    pygame.display.update()
