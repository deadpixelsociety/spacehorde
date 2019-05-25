package com.spacehorde.ships

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils

open class ShipMask {
    var width = 0
        private set
    var height = 0
        private set

    private lateinit var cells: Array<IntArray>

    constructor(file: String) {
        load("ships\\$file.txt")
    }

    private constructor(width: Int, height: Int, dataWidth: Int, dataHeight: Int, data: Array<IntArray>) {
        this.width = width
        this.height = height

        cells = Array(height) { IntArray(width) { Cell.EMPTY.value } }
        for (y in 0 until dataHeight) {
            for (x in 0 until dataWidth) {
                cells[y][x] = data[y][x]
            }
        }
    }

    operator fun get(x: Int, y: Int) = Cell.byValue(cells[y][x])

    operator fun set(x: Int, y: Int, cell: Cell) {
        cells[y][x] = cell.value
    }

    fun copy(width: Int = this.width) = ShipMask(width, height, this.width, height, cells)

    fun generateRandomFullMask(): ShipMask {
        val mask = randomize()
        createOuterHull(mask)
        return verticalMirror(mask)
    }

    private fun randomize(): ShipMask {
        val mask = copy()

        // randomize new mask cells based on existing mask.
        for (y in 0 until height) {
            for (x in 0 until width) {
                when (get(x, y)) {
                    Cell.BODY -> {
                        // Body either stays a body or becomes empty.
                        mask[x, y] = if (MathUtils.randomBoolean()) Cell.BODY else Cell.EMPTY
                    }
                    Cell.EMPTY -> {
                        // nop
                    }
                    Cell.COCKPIT -> {
                        // Cockpit either stays a cockpit or becomes a hull OR a body.
                        if (MathUtils.randomBoolean()) {
                            mask[x, y] = Cell.COCKPIT
                        } else {
                            mask[x, y] = Cell.HULL
                            /*
                            if (MathUtils.randomBoolean()) {
                                mask[x, y] = Cell.BODY
                            } else {
                                mask[x, y] = Cell.HULL
                            }*/
                        }
                    }
                    Cell.HULL -> {
                        // nop
                    }
                }
            }
        }

        return mask
    }

    private fun createOuterHull(mask: ShipMask): ShipMask {
        // fill all empty spaces around body cells with hull.
        val neighbors = listOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1))
        for (y in 0 until mask.height) {
            for (x in 0 until mask.width) {
                if (mask[x, y] == Cell.BODY) {
                    neighbors.forEach {
                        val x0 = x + it.first
                        val y0 = y + it.second
                        if (mask.withinBounds(x0, y0) && mask[x0, y0] == Cell.EMPTY) mask[x0, y0] = Cell.HULL
                    }
                }
            }
        }

        return mask
    }

    private fun verticalMirror(mask: ShipMask): ShipMask {
        val fullMask = mask.copy(mask.width * 2)

        for (y in 0 until mask.height) {
            for (x in 0 until mask.width) {
                fullMask[fullMask.width - 1 - x, y] = mask[x, y]
            }
        }

        return fullMask
    }

    fun withinBounds(x: Int, y: Int) = x in 0 until width && y in 0 until height

    private fun load(maskFile: String) {
        load(Gdx.files.internal(maskFile))
    }

    private fun load(maskFile: FileHandle) {
        val data = maskFile.readString()
        val lines = data.split("\n")

        width = lines[0].trim().length
        height = lines.size

        cells = Array(height) { IntArray(width) { Cell.EMPTY.value } }

        for (y in 0 until lines.size) {
            val line = lines[y].trim()
            if (line.isEmpty()) continue
            for (x in 0 until line.length) {
                set(x, y, Cell.byValue(line[x].toString().toInt()))
            }
        }
    }

    override fun toString(): String {
        return StringBuilder().let {
            for (y in 0 until height) {
                for (x in 0 until width) {
                    it.append(get(x, y).value)
                }

                it.appendln()
            }

            it.toString()
        }
    }

    enum class Cell(val value: Int, val color: Color) {
        EMPTY(0, Color(0f, 0f, 0f, 0f)),
        BODY(1, Color(1f, 1f, 1f, 1f)),
        COCKPIT(2, Color(0f, 1f, 1f, 1f)),
        HULL(3, Color(0f, 0f, 0f, 1f));

        companion object {
            fun byColor(color: Color) = Cell.values().firstOrNull { it.color == color } ?: EMPTY
            fun byValue(value: Int) = Cell.values().firstOrNull { it.value == value } ?: EMPTY
        }
    }
}