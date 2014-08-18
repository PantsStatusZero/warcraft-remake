/*
 * Copyright (C) 2013-2014 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package com.b3dgs.warcraft.skill;

import java.util.Locale;

import com.b3dgs.lionengine.TextStyle;
import com.b3dgs.lionengine.core.Core;
import com.b3dgs.lionengine.core.Graphic;
import com.b3dgs.lionengine.core.Media;
import com.b3dgs.lionengine.core.Text;
import com.b3dgs.lionengine.drawable.Sprite;
import com.b3dgs.lionengine.drawable.SpriteTiled;
import com.b3dgs.lionengine.game.ContextGame;
import com.b3dgs.lionengine.game.FactoryObjectGame;
import com.b3dgs.lionengine.game.TimedMessage;
import com.b3dgs.lionengine.game.strategy.CameraStrategy;
import com.b3dgs.lionengine.game.strategy.ControlPanelModel;
import com.b3dgs.lionengine.game.strategy.CursorStrategy;
import com.b3dgs.lionengine.game.strategy.skill.SkillStrategy;
import com.b3dgs.warcraft.AppWarcraft;
import com.b3dgs.warcraft.RaceType;
import com.b3dgs.warcraft.entity.Entity;

/**
 * Default skill implementation.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
public abstract class Skill
        extends SkillStrategy
{
    /**
     * Get a skill configuration file.
     * 
     * @param race The race type.
     * @param type The config associated class.
     * @return The media config.
     */
    public static Media getConfig(RaceType race, Class<? extends Skill> type)
    {
        return Core.MEDIA.create(AppWarcraft.SKILLS_DIR, race.name().toLowerCase(Locale.ENGLISH), type.getSimpleName()
                + "." + FactoryObjectGame.FILE_DATA_EXTENSION);
    }

    /** Text. */
    protected final Text text;
    /** Sprite. */
    private final SpriteTiled icon;
    /** Gold icon. */
    private final Sprite gold;
    /** Wood icon. */
    private final Sprite wood;
    /** Owner. */
    protected Entity owner;
    /** Background. */
    private SpriteTiled background;
    /** Timed message handler. */
    private TimedMessage message;
    /** Location x on panel. */
    private int x;
    /** Location y on panel. */
    private int y;

    /**
     * Constructor.
     * 
     * @param setup The setup skill reference.
     */
    protected Skill(SetupSkill setup)
    {
        super(setup);
        text = Core.GRAPHIC.createText(Text.DIALOG, 10, TextStyle.NORMAL);
        icon = setup.icon;
        gold = setup.gold;
        wood = setup.wood;
    }

    /**
     * Set the skill owner.
     * 
     * @param entity The skill owner.
     */
    public void setOwner(Entity entity)
    {
        owner = entity;
    }

    /**
     * Prepare skill location.
     */
    public void prepare()
    {
        final int i = getPriority();
        x = 4 + (icon.getTileWidth() + 5) * (i % 2);
        y = 114 + (icon.getTileHeight() + 5) * (i / 2);
    }

    /*
     * SkillStrategy
     */

    @Override
    public void prepare(ContextGame context)
    {
        message = context.getService(TimedMessage.class);
        background = context.getService(SpriteTiled.class);
    }

    @Override
    public void updateOnMap(double extrp, CameraStrategy camera, CursorStrategy cursor)
    {
        // Nothing to do
    }

    @Override
    public void renderOnMap(Graphic g, CursorStrategy cursor, CameraStrategy camera)
    {
        // Nothing to do
    }

    @Override
    public void renderOnPanel(Graphic g)
    {
        final int flag = isSelected() ? 1 : 0;
        background.render(g, flag, x - 2, y - 2);
        icon.render(g, getLevel() - 1, x, y + flag);
        if (isOver() && !message.hasMessage())
        {
            text.draw(g, 72, 191, getDescription());
            if (this instanceof SkillProduceBuilding || this instanceof SkillProduceEntity)
            {
                final int width = text.getStringWidth(g, super.getDescription()) + 76;
                gold.render(g, width, 191);
                wood.render(g, width + 40, 191);
            }
        }
    }

    @Override
    public void onClicked(ControlPanelModel<?> panel)
    {
        // Nothing to do
    }

    @Override
    public boolean isOver(CursorStrategy cursor)
    {
        return cursor.getScreenX() >= x && cursor.getScreenX() <= x + icon.getTileWidth() && cursor.getScreenY() >= y
                && cursor.getScreenY() <= y + icon.getTileHeight();
    }
}
