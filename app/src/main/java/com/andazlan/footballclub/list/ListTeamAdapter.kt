package com.andazlan.footballclub.list

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.andazlan.footballclub.R
import com.andazlan.footballclub.model.Team
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class ListTeamAdapter(private val teams: List<Team>, private val listener: (Team) -> Unit) : RecyclerView.Adapter<ListTeamHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListTeamHolder {
        return ListTeamHolder(TeamUI().createView(AnkoContext.create(parent.context, parent)))
    }

    override fun getItemCount(): Int {
        return teams.size
    }

    override fun onBindViewHolder(holder: ListTeamHolder, position: Int) {
        holder.bindItem(teams[position], listener)
    }
}

class ListTeamHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val teamBadge: ImageView = view.find(R.id.team_badge)
    private val teamName: TextView = view.find(R.id.team_name)

    fun bindItem(team: Team, listener: (Team) -> Unit) {
        Picasso.with(itemView.context).load(team.teamBadge).into(teamBadge)
        teamName.text = team.teamName
        itemView.onClick { listener(team) }
    }
}

class TeamUI : AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>): View {
        return with(ui) {
            linearLayout {
                lparams(width = matchParent, height = wrapContent)
                padding = dip(16)
                orientation = LinearLayout.HORIZONTAL

                imageView {
                    id = R.id.team_badge
                }.lparams{
                    height = dip(50)
                    width = dip(50)
                }

                textView {
                    id = R.id.team_name
                    textSize = 16f
                }.lparams{
                    margin = dip(15)
                }
            }
        }
    }
}