package ca.encodeous.mwx.command.commands;

import ca.encodeous.mwx.command.Command;
import ca.encodeous.mwx.command.MissileWarsCommand;
import ca.encodeous.mwx.command.RootCommand;
import ca.encodeous.mwx.core.game.CoreGame;
import ca.encodeous.mwx.core.game.MissileWarsMatch;
import ca.encodeous.mwx.engines.lobby.Lobby;
import ca.encodeous.mwx.engines.lobby.LobbyEngine;
import ca.encodeous.mwx.core.lang.Strings;
import ca.encodeous.mwx.core.utils.Utils;
import org.bukkit.entity.Player;

public class wipe extends MissileWarsCommand {

    @Override
    public RootCommand BuildCommand() {
        return new RootCommand("wipe", Command::DefaultRestrictedCommand).Executes(context -> {
            MissileWarsMatch match = LobbyEngine.FromPlayer(context.GetPlayer());
            Lobby lobby = match != null ? match.lobby : null;
            if(lobby == null) {
                context.SendMessage(Strings.LOBBY_COMMAND);
                return 0;
            }
            if(!Utils.CheckPrivPermission(context.GetPlayer())) return 0;
            if(match.Map.isBusy || match.endCounter.isRunning() || match.startCounter.isRunning()){
                context.SendMessage("&cThe map cannot be wiped at this time!");
                return 0;
            }
            for(Player p : match.lobby.GetPlayers())
                CoreGame.GetImpl().SendTitle(p, "&9The map is being wiped", "&9by " + context.GetPlayer().getDisplayName() + "&r.");
            match.Wipe(() -> lobby.SendMessage("&9The lobby has been wiped"));
            return 1;
        });
    }

    @Override
    public String GetCommandName() {
        return "wipe";
    }
}
