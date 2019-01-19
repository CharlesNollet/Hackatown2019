import * as express from 'express';

const mongoose = require('mongoose');
mongoose.connect('mongodb://hack:Hackatown2019@ds159574.mlab.com:59574/hackdb', {useNewUrlParser: true});

const Game = mongoose.model('Game', { name: String });

export class Index {
    public router = express.Router();

    constructor() {
        this.router.get('/', this.index.bind(this));
        this.router.post('/test', this.test.bind(this));
        this.router.get('/games', this.getAllGames.bind(this));
    }

    private index(req: express.Request, res: express.Response) {
        res.json({
            version: process.env.VERSION
        });
    }

    private async test(req: express.Request, res: express.Response) {
        const game = new Game(req.body);
        await game.save();
        res.end();
    }

    private async getAllGames(req: express.Request, res: express.Response) {
        res.send(await Game.find());
    }
}
