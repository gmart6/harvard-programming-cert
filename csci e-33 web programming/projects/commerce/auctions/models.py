from django.contrib.auth.models import AbstractUser
from django.db import models

# Sourced from Django's reference on models and associated fields
class Category(models.Model):
    name = models.CharField(max_length=20)

    def __str__(self):
        return self.name

class User(AbstractUser):
    watchlist = models.ManyToManyField("Listing", related_name="watchlisters")

class Listing(models.Model):
    description = models.TextField(blank=True)
    listing_time = models.DateTimeField(auto_now=True)
    image_url = models.URLField(blank=True)
    isActive = models.BooleanField(default=True)
    opening_bid = models.DecimalField(max_digits=10, decimal_places=2)
    title = models.TextField(max_length=100)

    category = models.ForeignKey("Category", on_delete=models.SET_NULL, blank=True, null=True, related_name="listings")
    lister = models.ForeignKey("User", on_delete=models.CASCADE, related_name="listings")

    def __str__(self):
        return self.title

    def highest_bid(self):
        checkBids = self.bids.all()
        if not checkBids:
            return None
        else:
            return max(self.bids.all())
    
    def current_price(self):
        bid = self.highest_bid()
        if bid is not None:
            return bid.amount
        else:
            return self.opening_bid       

class Bid(models.Model):
    amount = models.DecimalField(max_digits=20, decimal_places=2)
    bidder = models.ForeignKey("User", on_delete=models.CASCADE, related_name="bids")
    bid_time = models.DateTimeField(auto_now=True)
    listing = models.ForeignKey(Listing, on_delete=models.CASCADE, related_name="bids")

    def __str__(self):
        return f"Bid of {self.amount} for {self.listing}"


class Comment(models.Model):
    text = models.CharField(max_length=200, blank=True)
    comment_time = models.DateTimeField(auto_now=True)
    commenter = models.ForeignKey("User", on_delete=models.CASCADE, related_name="comments")
    listing = models.ForeignKey("Listing", on_delete=models.CASCADE, related_name="comments")

    def __str__(self):
        return self.text
