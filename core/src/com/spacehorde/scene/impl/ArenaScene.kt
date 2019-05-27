package com.spacehorde.scene.impl

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.FillViewport
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.spacehorde.AudioManager
import com.spacehorde.Groups
import com.spacehorde.SpaceHordeGame
import com.spacehorde.components.*
import com.spacehorde.entities.Entities
import com.spacehorde.entities.WaveTracker
import com.spacehorde.entities.spawners.*
import com.spacehorde.graphics.Fonts
import com.spacehorde.scene.SceneImpl
import com.spacehorde.scoring.ScoreKeeper
import com.spacehorde.service.service
import com.spacehorde.systems.*

class ArenaScene : SceneImpl() {
    companion object {
        const val ARENA_SIZE = 500f
    }

    private val engine = Engine()
    private val viewport by lazy { FillViewport(600f, 600f) }
    private val uiViewport by lazy { ScreenViewport() }
    private val batch by service<SpriteBatch>()
    private val physicsMapper by mapper<Box2DPhysics>()
    private val playerPos = Vector2()
    private val spawnPos = Vector2()
    private val spawnCircle = Circle()
    private val waveSpawner = WaveSpawner(ARENA_SIZE)
    private var bombs = 3
    private var dead = false

    override fun create() {
        createSystems()

        val hs = ARENA_SIZE * .5f

        Entities.wall(engine, -hs, -hs, 5f, ARENA_SIZE)
        Entities.wall(engine, hs, -hs, 5f, ARENA_SIZE)
        Entities.wall(engine, -hs, -hs, ARENA_SIZE + 5f, 5f)
        Entities.wall(engine, -hs, hs - 5f, ARENA_SIZE + 5f, 5f)

        Entities.player(engine, 0f, 0f, true)

        Entities.camera(engine, viewport.camera as OrthographicCamera)

        // Prime physics
        engine.getSystem(Box2DSystem::class.java).update(1f)

        AudioManager.playMusic()

        ScoreKeeper.newShipEvent = {
            calcPlayerPosition()
            Entities.player(engine, playerPos.x, playerPos.y)
            AudioManager.playNewShip()
        }

        ScoreKeeper.newBombEvent = {
            bombs++
            AudioManager.playNewBomb()
        }

        engine.getSystem(ControlSystem::class.java).bombEvent = {
            if (bombs > 0) {
                bombs--
                moon2H()
                AudioManager.playBomb()
            }
        }

        engine.getSystem(ControlSystem::class.java).startEvent = {
            if (dead) {
                clearDudes()
                Entities.player(engine, 0f, 0f, true)
                dead = false
                ScoreKeeper.score = 0
                WaveTracker.reset()
                bombs = 3
            }
        }
    }

    private fun moon2H() {
        engine.entities.forEach {
            val group = it.getComponent(GroupMask::class.java)
            if (group != null && (group.match(Groups.ENEMIES) || group.match(Groups.SHRAPNEL))) {
                it.add(component<Dying>(engine))
            }
        }
    }

    private fun clearDudes() {
        engine.entities.forEach {
            val group = it.getComponent(GroupMask::class.java)
            if (group != null && (group.match(Groups.ENEMIES) || group.match(Groups.SHRAPNEL))) {
                engine.removeEntity(it)
            }
        }
    }

    private fun calcSpawnPos(radius: Float) {
        var attempts = 10
        val expanded = radius * 1.1f
        val hw = ARENA_SIZE * .5f - expanded
        spawnPos.set(MathUtils.random(-hw, hw), MathUtils.random(-hw, hw))
        spawnCircle.set(spawnPos, expanded * 1.1f)
        while (spawnCircle.contains(playerPos) && attempts > 0) {
            spawnPos.set(MathUtils.random(-hw, hw), MathUtils.random(-hw, hw))
            spawnCircle.set(spawnPos, expanded * 1.1f)
            attempts--
        }
    }

    private fun spawnCrosses() {
        calcPlayerPosition()
        val spawnRadius = 50f
        calcSpawnPos(spawnRadius)
        EnemyCrossSpawner(spawnPos, spawnRadius).spawn(engine, MathUtils.random(5, 12))
    }

    private fun spawnCircles() {
        calcPlayerPosition()
        val spawnRadius = 50f
        calcSpawnPos(spawnRadius)
        EnemyCircleSpawner(spawnPos, spawnRadius).spawn(engine, MathUtils.random(3, 6))
    }

    private fun spawnSmall() {
        calcPlayerPosition()
        val spawnRadius = 50f
        calcSpawnPos(spawnRadius)
        EnemySmallSpawner(spawnPos, spawnRadius).spawn(engine, MathUtils.random(5, 10))
    }

    private fun spawnPinwheels() {
        calcPlayerPosition()
        val spawnRadius = 50f
        calcSpawnPos(spawnRadius)
        EnemyPinwheelSpawner(spawnPos, spawnRadius).spawn(engine, MathUtils.random(1, 3))
    }

    private fun spawnDiamonds() {
        calcPlayerPosition()
        val spawnRadius = 50f
        calcSpawnPos(spawnRadius)
        EnemyDiamondSpawner(spawnPos, spawnRadius).spawn(engine, MathUtils.random(5, 10))
    }

    private fun createSystems() {
        engine.addSystem(GraveyardSystem())
        engine.addSystem(TagSystem())
        engine.addSystem(GroupSystem())
        engine.addSystem(ControlSystem())
        engine.addSystem(ScriptSystem())
        engine.addSystem(Box2DSystem())
        engine.addSystem(SpatialSystem(ARENA_SIZE, ARENA_SIZE))
        engine.addSystem(DollySystem())
        engine.addSystem(RenderSystem(viewport.camera))
        engine.addSystem(TextRenderSystem(viewport.camera, uiViewport.camera))
        if (SpaceHordeGame.DEBUG) engine.addSystem(DebugRenderSystem(viewport.camera))
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
        uiViewport.update(width, height, true)
    }

    override fun update(dt: Float, tt: Float) {
        WaveTracker.update(dt)
        engine.update(dt)

        if (WaveTracker.canSpawnWave()) {
            WaveTracker.startWave()
            var spawnCount = 2 + (MathUtils.random(1, 10) * WaveTracker.difficulty).toInt()
            for (i in 0 until spawnCount) waveSpawner.spawn(engine, 0)
        }

        val enemies = engine.getSystem(GroupSystem::class.java)[Groups.ENEMIES]
        if (enemies.size <= 3) WaveTracker.finishWave()

        val players = engine.getSystem(GroupSystem::class.java)[Groups.PLAYERS]
        dead = players.size == 0

        when {
            Gdx.input.isKeyJustPressed(Input.Keys.SPACE) -> Entities.player(engine, 0f, 0f)
            Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) -> spawnCircles()
            Gdx.input.isKeyJustPressed(Input.Keys.NUM_2) -> spawnSmall()
            Gdx.input.isKeyJustPressed(Input.Keys.NUM_3) -> spawnCrosses()
            Gdx.input.isKeyJustPressed(Input.Keys.NUM_4) -> spawnPinwheels()
            Gdx.input.isKeyJustPressed(Input.Keys.NUM_5) -> spawnDiamonds()
            Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE) -> moon2H()
        }
    }

    private fun calcPlayerPosition() {
        val players = engine.getSystem(GroupSystem::class.java)[Groups.PLAYERS]
        var px = 0f
        var py = 0f

        players.forEach {
            val physics = physicsMapper.get(it)
            val body = physics.body
            px += body?.worldCenter?.x ?: 0f
            py += body?.worldCenter?.y ?: 0f
        }

        if (players.isNotEmpty()) {
            px /= players.size.toFloat()
            py /= players.size.toFloat()
        }

        playerPos.set(px, py)
    }

    override fun draw(dt: Float, tt: Float) {
        Gdx.gl.glClearColor(.05f, .05f, .05f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        viewport.apply()
        engine.getSystem(RenderSystem::class.java)?.update(dt)
        engine.getSystem(TextRenderSystem::class.java)?.update(dt)
        if (SpaceHordeGame.DEBUG) engine.getSystem(DebugRenderSystem::class.java)?.update(dt)

        renderUI()
    }

    override fun dispose() {
    }

    private fun renderUI() {
        uiViewport.apply()
        batch.projectionMatrix = uiViewport.camera.combined
        batch.begin()

        drawHighScore()
        drawScore()
        drawBombs()

        if (dead) {
            drawDead()
        }

        batch.end()
    }

    private fun drawDead() {
        val text = "PRESS START TO GO AGANE"
        Fonts.glyphLayout.setText(Fonts.scoreUI, text)
        Fonts.scoreUI.draw(batch, text, (Gdx.graphics.width - Fonts.glyphLayout.width) * .5f, (Gdx.graphics.height - Fonts.glyphLayout.height) * .5f)
    }

    private fun drawBombs() {
        val text = "BOMBS $bombs"
        Fonts.glyphLayout.setText(Fonts.scoreUI, text)
        Fonts.scoreUI.draw(batch, text, (Gdx.graphics.width - Fonts.glyphLayout.width) * .5f, Gdx.graphics.height - 32f - Fonts.glyphLayout.height)
    }

    private fun drawScore() {
        Fonts.glyphLayout.setText(Fonts.scoreUI, "SCORE")
        Fonts.scoreUI.draw(batch, "SCORE", Gdx.graphics.width - 32f - Fonts.glyphLayout.width, Gdx.graphics.height - 32f - Fonts.glyphLayout.height)
        val score = ScoreKeeper.score.toString()
        Fonts.glyphLayout.setText(Fonts.scoreUI, score)
        Fonts.scoreUI.draw(batch, score, Gdx.graphics.width - 32f - Fonts.glyphLayout.width, Gdx.graphics.height - 32f - Fonts.glyphLayout.height - 16f)
    }

    private fun drawHighScore() {
        Fonts.glyphLayout.setText(Fonts.scoreUI, "HIGH SCORE")
        Fonts.scoreUI.draw(batch, "HIGH SCORE", 32f, Gdx.graphics.height - 32f - Fonts.glyphLayout.height)
        Fonts.scoreUI.draw(batch, ScoreKeeper.highScore.toString(), 32f, Gdx.graphics.height - 32f - Fonts.glyphLayout.height - 16f)
    }
}