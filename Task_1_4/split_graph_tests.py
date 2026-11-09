#!/usr/bin/env python3
"""Split Task_1_4 MainTest into focused JUnit 5 test classes."""
from pathlib import Path
import re
import sys

root = Path(sys.argv[1] if len(sys.argv) > 1 else 'Task_1_4')
folder = root / 'src/test/java/ru/nsu/legkov/graphs'
original = folder / 'MainTest.java'
source = original.read_text(encoding='utf-8')

# Extract method bodies with balanced Java braces; existing test methods do not
# contain braces in string literals, so this is safe for the current revision.
def extract(name):
    match = re.search(r'    @Test\s+    void ' + re.escape(name) + r'\([^)]*\)(?: throws \w+)? \{', source)
    if not match:
        raise ValueError('Test method missing from MainTest: ' + name)
    opening = source.index('{', match.start())
    depth = 0
    for pos in range(opening, len(source)):
        if source[pos] == '{':
            depth += 1
        elif source[pos] == '}':
            depth -= 1
            if depth == 0:
                return source[match.start():pos + 1]
    raise ValueError('Unbalanced method: ' + name)

common = '''package ru.nsu.legkov.graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.nsu.legkov.Edge;
import ru.nsu.legkov.Vertex;
import ru.nsu.legkov.sort.DfsSort;
import ru.nsu.legkov.sort.KahnSort;
import ru.nsu.legkov.sort.TopologicalSort;
'''

groups = {
    'VertexEdgeTest': ['vertexAndEdgeValueObjects'],
    'GraphOperationsTest': ['addVerticesAndEdges', 'removeEdgesAndVertices',
                            'removingMiddleVertexKeepsOtherEdges', 'loopsAndReverseEdgesAreDistinct',
                            'growMatricesBeyondInitialCapacity', 'growIncidenceEdgeCapacity'],
    'GraphEqualityTest': ['graphEqualityAcrossImplementations'],
    'GraphFileIoTest': ['saveLoadRoundTrip', 'loadWithCustomParserAndFormatter',
                        'loadInvalidFiles', 'emptyGraphAndFile'],
    'TopologicalSortTest': ['dfsAndKahnProduceValidTopologicalOrder',
                            'sortsHandleCyclesAndEmptyGraphs'],
}

helper = '''package ru.nsu.legkov.graphs;

import java.util.List;
import ru.nsu.legkov.Vertex;

/** Shared factories for graph tests. */
final class GraphTestSupport {
    private GraphTestSupport() {
    }

    static List<Graph<String>> graphs() {
        return List.of(
                new AdjacencyListGraph<>(),
                new AdjacencyMatrixGraph<>(),
                new IncidenceMatrixGraph<>());
    }

    static Vertex<String> vertex(int id) {
        return new Vertex<>(id, "V" + id);
    }
}
'''

for cls, names in groups.items():
    methods = '\n\n'.join(extract(name) for name in names)
    methods = re.sub(r'(?<![.\w])graphs\(\)', 'GraphTestSupport.graphs()', methods)
    methods = re.sub(r'(?<![.\w])vertex\(', 'GraphTestSupport.vertex(', methods)
    # Import only symbols referenced in this test class.
    imports = []
    for line in common.splitlines():
        if not line.startswith('import '):
            continue
        symbol = line.split('.')[-1].removesuffix(';')
        if re.search(r'\b' + re.escape(symbol) + r'\b', methods):
            imports.append(line)
    # JUnit Test annotation is required for every class.
    imports.append('import org.junit.jupiter.api.Test;')
    imports = sorted(set(imports), key=lambda line: (not line.startswith('import static '), line))
    header = 'package ru.nsu.legkov.graphs;\n\n' + '\n'.join(imports) + '\n\n'
    if cls == 'GraphFileIoTest':
        imports.append('import org.junit.jupiter.api.io.TempDir;')
        header = 'package ru.nsu.legkov.graphs;\n\n' + '\n'.join(sorted(set(imports), key=lambda line: (not line.startswith('import static '), line))) + '\n\n'
        header += '/** Tests for graph serialization and parsing. */\n'
    elif cls == 'TopologicalSortTest':
        header += '/** Tests for the DFS and Kahn topological sorting algorithms. */\n'
    elif cls == 'GraphOperationsTest':
        header += '/** Tests for all three graph implementations. */\n'
    elif cls == 'GraphEqualityTest':
        header += '/** Tests for graph equality across implementations. */\n'
    else:
        header += '/** Tests for vertex and edge value objects. */\n'
    content = header + 'class ' + cls + ' {\n'
    if cls == 'GraphFileIoTest':
        content += '    @TempDir Path tempDir;\n\n'
    content += methods + '\n}\n'
    (folder / (cls + '.java')).write_text(content, encoding='utf-8')

(folder / 'GraphTestSupport.java').write_text(helper, encoding='utf-8')
original.unlink()
print('Created:', ', '.join(sorted(p.name for p in folder.glob('*.java'))))
print('Preserved', sum(map(len, groups.values())), 'existing tests')
