import * as express from 'express';

const mongoose = require('mongoose');
mongoose.connect('mongodb://hack:Hackatown2019@ds159574.mlab.com:59574/hackdb', {useNewUrlParser: true});

const Game = mongoose.model('Game', { name: String });
const Player = mongoose.model('Player', {username: String, lat : Number, long: Number, tag: Boolean})

export class Index {
    public router = express.Router();

    constructor() {
        this.router.get('/', this.index.bind(this));
        // this.router.post('/postgame', this.postGame.bind(this));
        this.router.get('/games', this.getAllGames.bind(this));
        this.router.get('/getPlayer', this.getPlayers.bind(this));
        this.router.delete('/deletePlayer', this.deletePlayer.bind(this));
        this.router.post('/postPlayer', this.postPlayer.bind(this));
    }

    private index(req: express.Request, res: express.Response) {
        res.json({
            version: process.env.VERSION
        });
    }

    // private async postGame(req: express.Request, res: express.Response) {
    //     const game = new Game(req.body);
    //     await game.save();
    //     res.end();
    // }

    private async getAllGames(req: express.Request, res: express.Response) {
        res.send(await Game.find());
    }

    private async getPlayers(req: express.Request, res:express.Response) {
        res.send(await Player.find());
    }

    private async deletePlayer(req: express.Request, res:express.Response) {
        await this.getPlayers(req, res);
    }

    private async postPlayer(req:express.Request, res:express.Response) {
        const player = new Player(req.body);
        await player.save();
        res.end();
    }
}
