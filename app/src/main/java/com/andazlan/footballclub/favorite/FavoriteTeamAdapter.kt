package com.andazlan.footballclub.favorite

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.andazlan.footballclub.R
import com.andazlan.footballclub.model.Favorite
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class FavoriteTeamAdapter(private val favorites: List<Favorite>, private val listener: (Favorite) -> Unit) :
        RecyclerView.Adapter<FavoriteHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteHolder {
        return FavoriteHolder(TeamUI().createView(AnkoContext.create(parent.context, parent)))
    }

    override fun getItemCount(): Int {
        return favorites.size
    }

    override fun onBindViewHolder(holder: FavoriteHolder, position: Int) {
        holder.bindItem(favorites[position], listener)
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

class FavoriteHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val teamBadge: ImageView = view.find(R.id.team_badge)
    private val teamName: TextView = view.find(R.id.team_name)

    fun bindItem(favorite: Favorite, listener: (Favorite) -> Unit) {
        Picasso.with(itemView.context).load(favorite.teamBadge).into(teamBadge)
        teamName.text = favorite.teamName
        itemView.onClick { listener(favorite) }
    }
}
