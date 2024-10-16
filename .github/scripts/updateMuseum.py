import json
import requests
import os

outputJson = {}
weapons = set()
armor = set()
rarities = set()
special = set()
armorToID = {}
children = {}
maxValues = {}
itemToXp = {}
armorSets = {}
mappedIds = {}


def fetchJson(apiUrl):
    response = requests.get(apiUrl)
    return response.json()


def processMuseumData(internalName, data):
    itemType = data['type']

    if 'parent' in data:
        parentData = data['parent']
        if parentData is None:
            pass
        else:
            for parent in parentData:
                children[parentData[parent]] = internalName

    if 'mapped_item_ids' in data:
        for mappedId in data['mapped_item_ids']:
            mappedIds[mappedId] = internalName

    if itemType == 'ARMOR_SETS':
        donationXpInfo = data['armor_set_donation_xp']
        for armorSet in donationXpInfo:
            itemToXp[armorSet] = donationXpInfo[armorSet]
            armor.add(armorSet)
            addPieceToSet(internalName, armorSet)
        return

    else:
        donationXp = data['donation_xp']
        itemToXp[internalName] = donationXp

    if itemType == 'WEAPONS':
        weapons.add(internalName)
    elif itemType == 'RARITIES':
        rarities.add(internalName)


def addPieceToSet(piece, setName):
    if setName not in armorSets:
        armorSets[setName] = set()
    armorSets[setName].add(piece)


priorityExceptions = {
    "PERFECT_TIER_12": "PERFECT_HELMET_12",
    "PERFECT_TIER_13": "PERFECT_HELMET_13",
    "ARMOR_OF_THE_PACK": "HELMET_OF_THE_PACK",
    "SALMON_NEW": "SALMON_HELMET_NEW",
}

setPriorityList = [
    "HELMET",
    "NECKLACE",
    "HOOD",
    "HAT",
    "CHESTPLATE",
    "CLOAK",
]


def findAppropriateId(setName):
    if setName in priorityExceptions:
        armorToID[setName] = priorityExceptions[setName]
        return

    partsMap = {}
    for part in armorSets[setName]:
        partsMap[part] = part.split("_")[-1]

    priorityMap = {part: index for index, part in enumerate(setPriorityList)}

    sortedParts = sorted(partsMap.keys(), key=lambda part: priorityMap.get(partsMap[part], float('inf')))

    highestPriorityPart = sortedParts[0] if sortedParts else None

    if highestPriorityPart and partsMap[highestPriorityPart] not in priorityMap:
        print(f"Highest priority part for set {setName} was not found in setPriorityList. Parts: {partsMap}")

    armorToID[setName] = highestPriorityPart


if __name__ == '__main__':

    url = "https://api.hypixel.net/v2/resources/skyblock/items"
    fetchedJson = fetchJson(url)
    items = fetchedJson['items']

    for item in items:
        itemId = item['id']

        if 'museum' in item:
            special.add(itemId)

        if 'museum_data' in item:
            processMuseumData(itemId, item['museum_data'])

    for armorSet in armorSets:
        findAppropriateId(armorSet)

    maxValues['weapons'] = len(weapons)
    maxValues['armor'] = len(armor)
    maxValues['rarities'] = len(rarities)
    maxValues['special'] = 48
    maxValues['total'] = maxValues['weapons'] + maxValues['armor'] + maxValues['rarities']

    outputJson = {
        "weapons": sorted(list(weapons), key=lambda item: itemToXp.get(item, 0)),
        "armor": sorted(list(armor), key=lambda item: itemToXp.get(item, 0)),
        "rarities": sorted(list(rarities), key=lambda item: itemToXp.get(item, 0)),
        "special": sorted(list(special)),
        "armor_to_id": dict(sorted(armorToID.items())),
        "children": dict(sorted(children.items())),
        "max_values": maxValues,
        "itemToXp": dict(sorted(itemToXp.items())),
        "mapped_ids": dict(sorted(mappedIds.items())),
    }

    os.makedirs(os.path.dirname("constants/museum.json"), exist_ok=True)
    with open("constants/museum.json", "w") as json_file:
        json.dump(outputJson, json_file, indent=2)

    print(f"constants/museum.json")

