package me.lomexicano.module.iterators;

import me.lomexicano.ModuleInfo;
import net.eq2online.macros.scripting.ScriptedIterator;
import net.eq2online.macros.scripting.api.APIVersion;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.api.IScriptedIterator;
import net.minecraft.client.Minecraft;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.minecraft.util.StringUtils;

@APIVersion(ModuleInfo.API_VERSION)
public class ScriptedIteratorSideBar extends ScriptedIterator {
    public ScriptedIteratorSideBar() {
        super(null, null);
    }
    public ScriptedIteratorSideBar(IScriptActionProvider provider, IMacro macro) {
        super(provider, macro);
        Scoreboard scoreboard = Minecraft.getMinecraft().world.getScoreboard();
        for (ScoreObjective objective : scoreboard.getScoreObjectives()) {
            List<Score> scoreList = new ArrayList<>(scoreboard.getSortedScores(objective));

            this.begin();
            this.add("SIDEBARLINE", objective.getDisplayName());
            this.add("SIDEBARLINECLEAN", StringUtils.stripControlCodes(objective.getDisplayName()));
            this.add("SIDEBARTITLE",objective.getDisplayName());
            this.add("SIDEBARTITLECLEAN",StringUtils.stripControlCodes(objective.getDisplayName()));
            this.add("SIDEBARINDEX",0);
            this.add("SIDEBARREALINDEX",scoreList.size()+1);
            this.end();

            //provider.actionAddChatMessage("Real Index: "+scoreList.size()+1);
            for (int i = scoreList.size() - 1; i >= 0; i--) {
                Score score = scoreList.get(i);
                ScorePlayerTeam team = scoreboard.getPlayersTeam(score.getPlayerName());
                if (team != null) {
                    this.begin();
                    this.add("SIDEBARLINE", team.getPrefix() + team.getSuffix());
                    this.add("SIDEBARLINECLEAN", StringUtils.stripControlCodes(team.getPrefix() + team.getSuffix()));
                    this.add("SIDEBARTITLE",objective.getDisplayName());
                    this.add("SIDEBARTITLECLEAN",StringUtils.stripControlCodes(objective.getDisplayName()));
                    this.add("SIDEBARINDEX",scoreList.size()-i);
                    this.add("SIDEBARREALINDEX",i+1);
                    this.end();
                }
            }
        }

    }
    public String getIteratorName() {
        return "sidebar";
    }

    public Class<? extends IScriptedIterator> getIteratorClass() {
        return getClass();
    }

    public void onInit() {
        ScriptContext.getAvailableContexts().forEach(context -> context.getCore().registerIterator(getIteratorName(), getIteratorClass()));
    }
}
