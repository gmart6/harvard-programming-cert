import random
import re

from django.http import HttpResponseRedirect
from django.shortcuts import render
from django.urls import reverse

from . import util

# Render index page
def index(request):
    return render(request, "encyclopedia/index.html", {
        "entries": util.list_entries()
    })

# Edit an existing entry
def edit(request, title):
    if request.method == "POST":
        text = request.POST['text']
        util.save_entry(title, text)
        return HttpResponseRedirect('/wiki/' + title)
    else:
        content = util.get_entry(title)
        return render(request, "encyclopedia/edit.html", {
            "title": title,
            "content": content
        })     


# Handle entry pages
def entry(request, title):
    content = util.get_entry(title)
    # Bring up error page if entry not found
    if content is None:
        return render(request, "encyclopedia/error_page.html", {
            "title":title
        })
    else:
        # Via regular expressions, parse markdown and convert to html
        bold = re.sub(r"\*\*(.*?)\*\*", "<strong>\\1</strong>", content)
        listed = re.sub(r"\*\s(.*)", "<ul><li>\\1</li></ul>", bold)
        fixlisted = re.sub(r"</ul>\s<ul>", "", listed)
        # Split the entry content into lines
        splitcontent = fixlisted.splitlines()
        newlist = []
        
        # Loop processes lines, continues conversion.
        # This could probably be a different function, but it's how I'd done it
        for line in splitcontent:
            pgph = re.sub(r"^((?!^<ul>|^#|^<li>|^</li>|^</ul>|^<\n>|^<\s>|^^$).*$)",
                         "<p>\\1</p>", line)
            heading6 = re.sub(r"^###### (.*?)$", "<h6>\\1</h6>", pgph)
            heading5 = re.sub(r"^##### (.*?)$", "<h5>\\1</h5>", heading6)
            heading4 = re.sub(r"^#### (.*?)$", "<h4>\\1</h4>", heading5)
            heading3 = re.sub(r"^### (.*?)$", "<h3>\\1</h3>", heading4)
            heading2 = re.sub(r"^## (.*?)$", "<h2>\\1</h2>", heading3)
            heading1 = re.sub(r"^# (.*?)$", "<h1>\\1</h1>", heading2)
            linked = re.sub(r"\[(.*?)\]\((.*?)\)",
                            "<a href='\\2'>\\1</a>", heading1)
            newlist.append(linked)

        return render(request, "encyclopedia/entry.html", {
            "content": newlist,
            "title": title
        })

# Creates new entry
def new_entry(request):
    # Handling standard access to the new entry page
    if request.method == "GET":
        error_msg = False
        return render(request, "encyclopedia/new_entry.html", {
            "error_msg": error_msg
        })
    # Handles pushing the new entry to the utility function
    else:
        title = request.POST['title']
        text = request.POST['text']
        entries = util.list_entries()
        lower_entries = [ch.lower() for ch in entries]
        if title.lower() in lower_entries:
            error_msg = True
            return render(request, "encyclopedia/new_entry.html", {
                "error_msg": error_msg,
                "title": title
            })
        else:
            util.save_entry(title, text)
            return HttpResponseRedirect('wiki/' + title)

# Provides random page
def random_page(request):
    response = random.choice(util.list_entries())
    # Pulled use of reverse and args from Stack Overflow
    return HttpResponseRedirect(reverse("entry", args=(response,)))

# Gives search functionality
def search(request):
    query = request.GET['q']
    lower_srch = query.lower()
    entries = util.list_entries()
    lower_entries = [ent.lower() for ent in entries]
    if lower_srch in lower_entries:
        return HttpResponseRedirect(reverse("entry", args=(lower_srch,)))

    results = [ent1 for ent1 in lower_entries if lower_srch in ent1]

    # Handles if no results
    if not results:
        return render(request, "encyclopedia/search_results.html", {
            "query": query
        })
    else:
        return render(request, "encyclopedia/results.html", {
            "results": results
        })


